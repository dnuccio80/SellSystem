package org.example.project.ui.screens.addproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.domain.models.Product
import org.example.project.domain.usecases.products.AddProduct

enum class UpdatableProductData {
    NAME, CATEGORY, BRAND, BUY_PRICE, LIST_PRICE, CASH_PRICE, CURRENT_STOCK, ADVICE_STOCK, DESCRIPTION, TOGGLE_HAS_VARIANTS, TOGGLE_MANAGE_STOCK
}

class AddProductViewModel(
    private val addProduct: AddProduct,
    private val repository: ProductRepositoryImpl

    ) : ViewModel() {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _uiState = MutableStateFlow<AddProductUiState>(AddProductUiState.Success())
    val uiState = _uiState.asStateFlow()

    fun tryAddProduct() {
        val state = _uiState.value as AddProductUiState.Success

        viewModelScope.launch {
            if (checkDataCorrect()) {
                val product = if(!state.product.manageStock) {
                    state.product.copy(currentStock = 0, adviceStock = 0)
                } else state.product

                async { addProduct(product) }.await()
                cleanProductData()
                _events.emit("Operación exitosa!")
            } else {
                _events.emit("Faltan rellenar datos!")
            }
        }
    }

    fun deleteProduct(onDone:() -> Unit) {
        val state = _uiState.value as AddProductUiState.Success

        viewModelScope.launch {
            async { repository.deleteProductById(state.product.id) }.await()
            cleanProductData()
            _events.emit("Operación exitosa!")
            onDone()
        }

    }

    fun updateProduct(data: UpdatableProductData, value: Any) {

        when (data) {
            UpdatableProductData.NAME -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> state.copy(product = state.product.copy(name = value as String))
                    else -> state
                }
            }

            UpdatableProductData.CATEGORY -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> state.copy(product = state.product.copy(category = value as String))
                    else -> state
                }
            }

            UpdatableProductData.BRAND -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> state.copy(product = state.product.copy(brand = value as String))
                    else -> state
                }
            }

            UpdatableProductData.BUY_PRICE -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> {
                        val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                        state.copy(product = state.product.copy(buyPrice = newValue))
                    }

                    else -> state
                }
            }

            UpdatableProductData.LIST_PRICE -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> {
                        val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                        state.copy(product = state.product.copy(listPrice = newValue))
                    }

                    else -> state
                }
            }

            UpdatableProductData.CASH_PRICE -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> {
                        val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                        state.copy(product = state.product.copy(cashPrice = newValue))
                    }

                    else -> state
                }
            }

            UpdatableProductData.CURRENT_STOCK -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> {
                        val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                        state.copy(product = state.product.copy(currentStock = newValue))
                    }

                    else -> state
                }
            }

            UpdatableProductData.ADVICE_STOCK -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> {
                        val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                        state.copy(product = state.product.copy(adviceStock = newValue))
                    }

                    else -> state
                }
            }

            UpdatableProductData.DESCRIPTION -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> state.copy(
                        product = state.product.copy(
                            description = value as String
                        )
                    )

                    else -> state
                }
            }

            UpdatableProductData.TOGGLE_HAS_VARIANTS -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> state.copy(hasVariants = !state.hasVariants)
                    else -> state
                }
            }


            UpdatableProductData.TOGGLE_MANAGE_STOCK -> _uiState.update { state ->
                when (state) {
                    is AddProductUiState.Success -> state.copy(product = state.product.copy(manageStock = !state.product.manageStock))
                    else -> state
                }
            }

        }
    }

    fun loadProduct(id:Int) {
        viewModelScope.launch {
            _uiState.update { AddProductUiState.Loading }
            val product = async { repository.getProductById(id) }.await()
            _uiState.update { AddProductUiState.Success(
                product = product
            ) }
        }
    }

    fun cleanProductData() {
        _uiState.update { state ->
            when (state) {
                is AddProductUiState.Success -> state.copy(
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
                        manageStock = true
                    ),
                    hasVariants = false,
                )
                else-> state
            }
        }
    }

    fun checkDataCorrect(): Boolean {

        val state = _uiState.value as AddProductUiState.Success

        return state.product.name.isNotBlank() &&
                state.product.category.isNotBlank() &&
                state.product.brand.isNotBlank() &&
                state.product.buyPrice > 0 &&
                state.product.listPrice > 0 &&
                state.product.cashPrice > 0 &&
                state.product.description.isNotBlank() &&
                if (state.product.manageStock) state.product.currentStock > 0 else state.product.currentStock >= 0
    }

}