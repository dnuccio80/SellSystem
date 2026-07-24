package org.example.project.ui.screens.productvariants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.db.repositoriesimpl.ProductVariantRepositoryImpl
import org.example.project.domain.models.ProductVariant

class ProductVariantsViewModel(private val productVariantRepo: ProductVariantRepositoryImpl) :
    ViewModel() {

    private val _variantsList = MutableStateFlow(listOf(""))
    val variants = _variantsList.asStateFlow()
    private val _variantTitle = MutableStateFlow("")
    val variantTitle = _variantTitle.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _allProductVariants = productVariantRepo.getAllProductVariants().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val allProductVariants = _allProductVariants


    fun addField() {
        _variantsList.update {
            it + ""
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

    fun cleanData() {
        _variantsList.value = listOf("")
        _variantTitle.value = ""
    }

    fun addNewProductVariant(onDone:() -> Unit) {
        if (isAllDataCorrect()) {
            viewModelScope.launch {
                val newProductVariant = ProductVariant(
                    name = _variantTitle.value,
                    variants = _variantsList.value
                )
                async {
                    productVariantRepo.addProductVariant(newProductVariant)
                }.await()
                onDone()
            }
        } else {
            viewModelScope.launch {
                _events.emit("Faltan rellenar datos!")
            }
        }
    }

    fun isAllDataCorrect(): Boolean {
        return _variantTitle.value.isNotBlank() && _variantsList.value.isNotEmpty()
    }
}