package org.example.project.ui.screens.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.promotions.PromotionCategory
import org.example.project.domain.models.promotions.PromotionType
import org.example.project.domain.models.promotions.PromotionsError
import org.example.project.domain.repositories.PromotionRepository
import org.example.project.domain.usecases.products.GetCategories
import org.example.project.domain.usecases.products.GetProducts
import org.example.project.domain.usecases.promotions.AddPromotion
import org.example.project.domain.usecases.promotions.GetAllPromotions
import org.example.project.ui.models.PromotionPresentation
import org.example.project.ui.screens.promotions.UpdatePromotionAction.*
import kotlin.Int

enum class UpdatePromotionAction {
    PROMOTION_TYPE, BUY_X, PAY_Y, PERCENT, PROMOTION_CATEGORY, BRAND, CATEGORY, SPECIFIC_PRODUCTS, TOGGLE_DATE, DATE
}

class PromotionsViewModel(
    getCategories: GetCategories,
    getAllProducts: GetProducts,
    private val addPromotion: AddPromotion,
    getAllPromotions: GetAllPromotions,
    private val repository: PromotionRepository

    ) : ViewModel() {

    private val _promotionData = MutableStateFlow(
        PromotionPresentation(
            promoteBy = PromotionCategory.BRAND,
            promotionType = PromotionType.BUY_X_PAY_Y
        )
    )
    val promotionData = _promotionData.asStateFlow()

    private val _categories =
        getCategories("").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val categories = _categories
    private val _products = getAllProducts("").stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    val products = _products

    private val _promotionList = getAllPromotions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val promotionList = _promotionList

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun updatePromotionData(action: UpdatePromotionAction, value: String) {
        when (action) {
            PROMOTION_TYPE -> {
                _promotionData.update { data ->
                    data.copy(
                        promotionType = PromotionType.entries.first { entries -> entries.title == value },
                        buyX = null,
                        payY = null,
                        percent = null
                    )
                }
            }

            BUY_X -> {
                _promotionData.update { data -> data.copy(buyX = value.toIntOrNull()) }
            }

            PAY_Y -> {
                _promotionData.update { data -> data.copy(payY = value.toIntOrNull()) }
            }

            PERCENT -> {
                _promotionData.update { data ->
                    data.copy(
                        percent = value.toIntOrNull()?.coerceIn(0, 100)
                    )
                }
            }

            PROMOTION_CATEGORY -> {
                _promotionData.update { data ->
                    data.copy(
                        promoteBy = PromotionCategory.entries.first { it.title == value },
                        brandSelected = null,
                        categoryListSelected = null,
                        specificProducts = null
                    )
                }
            }

            BRAND -> {
                _promotionData.update { data -> data.copy(brandSelected = value) }
            }

            CATEGORY -> {
                _promotionData.update { data -> data.copy(categoryListSelected = emptyList()) }
            }

            SPECIFIC_PRODUCTS -> {
                _promotionData.update { data -> data.copy(specificProducts = emptyList()) }
            }

            TOGGLE_DATE -> {
                _promotionData.update { data ->
                    val newValue = !data.hasDate
                    if (!newValue) {
                        data.copy(
                            hasDate = newValue,
                            hasDateInit = true,
                            hasDateEnd = true
                        )
                    } else {
                        data.copy(
                            hasDate = newValue,
                            promoStart = null,
                            promoEnd = null
                        )
                    }

                }
            }

            DATE -> {}
        }
    }

    fun updatePromotionListSelected(list: List<ProductCategory>) {
        _promotionData.update { it.copy(categoryListSelected = list) }
    }

    fun updateProductsListSelected(list: List<Product>) {
        _promotionData.update { it.copy(specificProducts = list) }
    }

    fun updatePromoDateInit(newValue: LocalDateTime) {
        _promotionData.update { it.copy(promoStart = newValue) }
    }

    fun toggleHasPromoDateInit() {
        _promotionData.update {
            val newValue = !it.hasDateInit
            if (!newValue) it.copy(hasDateInit = newValue, promoStart = null)
            else it.copy(hasDateInit = newValue)
        }
    }

    fun toggleHasPromoDateEnd() {
        _promotionData.update {
            val newValue = !it.hasDateEnd
            if (!newValue) it.copy(hasDateEnd = newValue, promoEnd = null)
            else it.copy(hasDateEnd = newValue)
        }
    }

    fun updatePromoDateEnd(newValue: LocalDateTime) {
        _promotionData.update { it.copy(promoEnd = newValue) }
    }

    fun addPromo(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                addPromotion(_promotionData.value)
                cleanPromotionData()
                onDone()
            } catch (e: PromotionsError) {
                _events.emit(e.msg)
            }
        }
    }

    fun updatePromotionDataById(id:Int) {
        val newData = _promotionList.value.first { it.promotion.id == id }
        _promotionData.update { newData.promotion }
    }

    fun deletePromotion() {
        viewModelScope.launch {
            repository.deletePromotionById(_promotionData.value.id)
            cleanPromotionData()
        }
    }

    fun cleanPromotionData() {
        _promotionData.update {
            PromotionPresentation(
                id = 0,
                promoteBy = PromotionCategory.BRAND,
                promotionType = PromotionType.BUY_X_PAY_Y,
                buyX = null,
                payY = null,
                percent = null,
                brandSelected = null,
                categoryListSelected = null,
                specificProducts = null,
                hasDate = false,
                hasDateInit = true,
                hasDateEnd = true,
                promoStart = null,
                promoEnd = null,
            )
        }
    }


}