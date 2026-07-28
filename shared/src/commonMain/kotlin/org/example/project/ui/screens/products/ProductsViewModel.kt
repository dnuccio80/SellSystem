package org.example.project.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.compositionUniqueId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.Product
import org.example.project.domain.usecases.products.AddProduct
import org.example.project.domain.usecases.products.GetProducts
import kotlin.time.Duration.Companion.milliseconds

enum class UpdatableProductData {
    NAME, CATEGORY, BRAND, BUY_PRICE, LIST_PRICE, CASH_PRICE, CURRENT_STOCK, ADVICE_STOCK, DESCRIPTION
}

class ProductsViewModel(
    getProducts: GetProducts,
    private val addProduct: AddProduct,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _manageStock = MutableStateFlow(false)
    val manageStock = _manageStock.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _product = MutableStateFlow(
        Product(
            name = "",
            category = "",
            brand = "",
            buyPrice = 0,
            listPrice = 0,
            cashPrice = 0,
            currentStock = 0,
            adviceStock = 0,
            description = ""
        )
    )
    val product = _product.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _products = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getProducts(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val products = _products

    fun updateQuery(newValue: String) {
        _query.value = newValue
    }

    fun tryAddProduct(onDone:() -> Unit) {
        viewModelScope.launch {
            if (checkDataCorrect()) {
                async { addProduct(_product.value) }.await()
                cleanProductData()
                onDone()
            } else {
                _events.emit("Faltan rellenar datos!")
            }
        }
    }

    fun toggleManageStock() {
        _manageStock.value = !_manageStock.value
    }

    fun updateProduct(data: UpdatableProductData, value: Any) {

        when (data) {
            UpdatableProductData.NAME -> _product.update { it.copy(name = value as String) }
            UpdatableProductData.CATEGORY -> _product.update { it.copy(category = value as String) }
            UpdatableProductData.BRAND -> _product.update { it.copy(brand = value as String) }
            UpdatableProductData.BUY_PRICE -> {
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                _product.update { it.copy(buyPrice = newValue) }
            }

            UpdatableProductData.LIST_PRICE -> {
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                _product.update { it.copy(listPrice = newValue) }
            }

            UpdatableProductData.CASH_PRICE -> {
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                _product.update { it.copy(cashPrice = newValue) }
            }

            UpdatableProductData.CURRENT_STOCK -> {
                val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                _product.update { it.copy(currentStock = newValue) }
            }

            UpdatableProductData.ADVICE_STOCK -> {
                val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                _product.update { it.copy(adviceStock = newValue) }
            }

            UpdatableProductData.DESCRIPTION -> _product.update { it.copy(description = value as String) }
        }
    }

    fun cleanProductData() {
        _product.update { current ->
            current.copy(
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
            )
        }
    }

    fun checkDataCorrect(): Boolean {
        return _product.value.name.isNotBlank() &&
                _product.value.category.isNotBlank() &&
                _product.value.brand.isNotBlank() &&
                _product.value.buyPrice > 0 &&
                _product.value.listPrice > 0 &&
                _product.value.cashPrice > 0 &&
                _product.value.description.isNotBlank() &&
                if (_manageStock.value) _product.value.currentStock > 0 else _product.value.currentStock >= 0
    }

}