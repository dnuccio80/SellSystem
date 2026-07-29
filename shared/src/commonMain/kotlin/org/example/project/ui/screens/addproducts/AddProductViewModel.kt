package org.example.project.ui.screens.addproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.Product
import org.example.project.domain.usecases.products.AddProduct

enum class UpdatableProductData {
    NAME, CATEGORY, BRAND, BUY_PRICE, LIST_PRICE, CASH_PRICE, CURRENT_STOCK, ADVICE_STOCK, DESCRIPTION, TOGGLE_HAS_VARIANTS, TOGGLE_MANAGE_STOCK
}

class AddProductViewModel(
    private val addProduct: AddProduct,

    ) : ViewModel() {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow(AddProductUiState.Success())
    val uiState = _uiState.asStateFlow()

    fun tryAddProduct() {
        viewModelScope.launch {
            if (checkDataCorrect()) {
                async { addProduct(_uiState.value.product) }.await()
                cleanProductData()
                _events.emit("Producto añadido con éxito!")
            } else {
                _events.emit("Faltan rellenar datos!")
            }
        }
    }

    fun updateProduct(data: UpdatableProductData, value: Any) {

        when (data) {
            UpdatableProductData.NAME -> _uiState.update {
                it.copy(product = it.product.copy(name = value as String))
            }

            UpdatableProductData.CATEGORY -> _uiState.update {
                it.copy(product = it.product.copy(category = value as String))
            }

            UpdatableProductData.BRAND -> _uiState.update {
                it.copy(product = it.product.copy(brand = value as String))
            }

            UpdatableProductData.BUY_PRICE -> _uiState.update {
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                it.copy(product = it.product.copy(buyPrice = newValue))
            }

            UpdatableProductData.LIST_PRICE -> _uiState.update {
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                it.copy(product = it.product.copy(listPrice = newValue))
            }

            UpdatableProductData.CASH_PRICE -> _uiState.update {
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                it.copy(product = it.product.copy(cashPrice = newValue))
            }

            UpdatableProductData.CURRENT_STOCK -> _uiState.update {
                val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                it.copy(product = it.product.copy(currentStock = newValue))
            }

            UpdatableProductData.ADVICE_STOCK -> _uiState.update {
                val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                it.copy(product = it.product.copy(adviceStock = newValue))
            }

            UpdatableProductData.DESCRIPTION -> _uiState.update {
                it.copy(product = it.product.copy(description = value as String))
            }

            UpdatableProductData.TOGGLE_HAS_VARIANTS -> _uiState.update { it.copy(hasVariants = !it.hasVariants) }
            UpdatableProductData.TOGGLE_MANAGE_STOCK -> _uiState.update { it.copy(manageStock = !it.manageStock) }
        }
    }

    fun cleanProductData() {
        _uiState.update {
            it.copy(
                product = Product(
                    id = 0,
                    name = "",
                    category = "",
                    brand = "",
                    buyPrice = 0L,
                    listPrice = 0L,
                    cashPrice = 0L,
                    currentStock = 0,
                    adviceStock = 0,
                    description = "",
                ),
                hasVariants = false,
                manageStock = true
            )
        }
    }

    fun checkDataCorrect(): Boolean {
        return _uiState.value.product.name.isNotBlank() &&
                _uiState.value.product.category.isNotBlank() &&
                _uiState.value.product.brand.isNotBlank() &&
                _uiState.value.product.buyPrice > 0 &&
                _uiState.value.product.listPrice > 0 &&
                _uiState.value.product.cashPrice > 0 &&
                _uiState.value.product.description.isNotBlank() &&
                if (_uiState.value.manageStock) _uiState.value.product.currentStock > 0 else _uiState.value.product.currentStock >= 0
    }

}