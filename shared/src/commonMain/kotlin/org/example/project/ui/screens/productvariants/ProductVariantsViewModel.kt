package org.example.project.ui.screens.productvariants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.product.ProductVariant
import org.example.project.domain.repositories.ProductVariantRepository
import org.example.project.domain.usecases.productvariants.AddProductVariant
import org.example.project.domain.usecases.productvariants.GetProductVariantById
import org.example.project.domain.usecases.productvariants.GetProductVariants
import kotlin.time.Duration.Companion.milliseconds

class ProductVariantsViewModel(
    private val addProductVariant: AddProductVariant,
    private val getProductVariants: GetProductVariants,
    private val getProductVariantById: GetProductVariantById,
    private val productVariantRepository: ProductVariantRepository,
) :
    ViewModel() {

    private val productVariantId = MutableStateFlow(0)
    private val _querySearch = MutableStateFlow("")
    val querySearch = _querySearch.asStateFlow()

    private val _variantsList = MutableStateFlow(listOf(""))
    val variants = _variantsList.asStateFlow()
    private val _variantTitle = MutableStateFlow("")
    val variantTitle = _variantTitle.asStateFlow()

    val productVariant = combine(
        productVariantId,
        _variantTitle,
        _variantsList
    ) { id, title, variants ->
        ProductVariant(
            id = id,
            name = title,
            variants = variants
        )
    }

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _allProductVariants =
        _querySearch.debounce(300.milliseconds).flatMapLatest { query ->
            getProductVariants(query)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), emptyList()
        )
    val allProductVariants = _allProductVariants


    fun addField(value: String = "") {
        _variantsList.update {
            it + value
        }
    }

    fun removeVariant(index: Int) {
        _variantsList.update { current ->
            current.toMutableList().apply {
                removeAt(index)
            }
        }
    }

    fun updateTitle(newValue: String) {
        _variantTitle.update { newValue }
    }

    fun updateValue(index: Int, value: String) {
        _variantsList.update { current ->
            current.toMutableList().apply {
                this[index] = value
            }
        }
    }

    fun updateQuerySearch(newValue: String) {
        _querySearch.value = newValue
    }

    fun cleanData() {
        productVariantId.value = 0
        _variantsList.value = listOf("")
        _variantTitle.value = ""
    }

    fun addNewProductVariant(onDone: () -> Unit) {
        if (isAllDataCorrect()) {
            viewModelScope.launch {
                async {
                    addProductVariant(productVariant.first())
                }.await()
                cleanData()
                onDone()
            }
        } else {
            viewModelScope.launch {
                _events.emit("Faltan rellenar datos!")
            }
        }
    }

    fun editProductVariant(id: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            val pv = async { getProductVariantById(id) }.await()
            productVariantId.value = id
            removeVariant(0)
            pv.variants.forEach { addField(it) }
            updateTitle(pv.name)
            onDone()
        }
    }

    fun deleteProductVariantById() {
        viewModelScope.launch {
            productVariantRepository.deleteProductVariantById(productVariantId.value)
        }
    }

    fun isAllDataCorrect(): Boolean {
        return _variantTitle.value.isNotBlank() && _variantsList.value.count { it.isNotBlank() } > 0
    }
}