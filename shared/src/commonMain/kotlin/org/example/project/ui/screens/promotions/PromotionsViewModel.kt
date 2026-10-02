package org.example.project.ui.screens.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.project.domain.models.promotions.PromotionCategory
import org.example.project.domain.models.promotions.PromotionType
import org.example.project.domain.usecases.products.GetCategories
import org.example.project.ui.models.PromotionPresentation
import org.example.project.ui.screens.promotions.UpdatePromotionAction.*

enum class UpdatePromotionAction {
    PROMOTION_TYPE, BUY_X, PAY_Y, PERCENT, PROMOTION_CATEGORY, BRAND, CATEGORY, SPECIFIC_PRODUCTS, TOGGLE_DATE, DATE
}

class PromotionsViewModel(private val getCategories: GetCategories) : ViewModel() {

    private val _promotionData = MutableStateFlow(
        PromotionPresentation(
            promoteBy = PromotionCategory.BRAND,
            promotionType = PromotionType.BUY_X_PAY_Y
        )
    )
    val promotionData = _promotionData.asStateFlow()

    private val _categories = getCategories("").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val categories = _categories

    fun updatePromotionData(action: UpdatePromotionAction, value: String) {
        when (action) {
            PROMOTION_TYPE -> {
                _promotionData.update { data -> data.copy(promotionType = PromotionType.entries.first { entries -> entries.title == value }) }
            }

            BUY_X -> {
                _promotionData.update { data -> data.copy(buyX = value.toIntOrNull()) }
            }

            PAY_Y -> {
                _promotionData.update { data -> data.copy(payY = value.toIntOrNull()) }
            }

            PERCENT -> {
                _promotionData.update { data -> data.copy(percent = value.toIntOrNull()) }
            }

            PROMOTION_CATEGORY -> {
                _promotionData.update { data -> data.copy(promoteBy = PromotionCategory.entries.first { it.title == value }) }
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
                _promotionData.update { data -> data.copy(hasDate = !data.hasDate) }
            }

            DATE -> {}
        }
    }

    fun cleanPromotionData() {
        _promotionData.update {
            PromotionPresentation(
                promoteBy = PromotionCategory.BRAND,
                promotionType = PromotionType.BUY_X_PAY_Y
            )
        }
    }


}