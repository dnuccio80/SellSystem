package org.example.project.ui.screens.addproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.domain.models.Product
import org.example.project.domain.usecases.products.AddProduct
import org.example.project.domain.usecases.products.CalculatePercentageProfitFromSellPrice
import org.example.project.domain.usecases.products.CalculatePriceFromPercentage
import org.example.project.ui.screens.products.CleanProduct

enum class UpdatableProductData {
    NAME, CATEGORY, BRAND, BUY_PRICE, LIST_PRICE, CASH_PRICE, CURRENT_STOCK, ADVICE_STOCK, DESCRIPTION, TOGGLE_HAS_VARIANTS, TOGGLE_MANAGE_STOCK
}

class AddProductViewModel(
    private val addProduct: AddProduct,
    private val repository: ProductRepositoryImpl,
    private val calculatePercentageProfitFromSellPrice: CalculatePercentageProfitFromSellPrice,
    private val calculatePriceFromPercentage: CalculatePriceFromPercentage

    ) : ViewModel() {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    val priceListType: List<String> = listOf(
        "Precio",
        "Porcentaje de ganancia"
    )
    val cashPriceType: List<String> = listOf(
        "Precio",
        "Porcentaje de descuento"
    )
    private val _product = MutableStateFlow(CleanProduct().getCleanProduct())
    private val _hasVariants = MutableStateFlow(false)
    private val _listPriceSelected = MutableStateFlow(priceListType.first())
    private val _cashPriceSelected = MutableStateFlow(cashPriceType.first())

    private val _uiState = combine(
        _product,
        _hasVariants,
        _listPriceSelected,
        _cashPriceSelected,
    ) { product, hasVariants, listPriceSelected, cashPriceSelected ->

        print("DAMIAN: $xx")
        print("DAMIAN 2: $xxx")

        AddProductUiState.Success(
            product = product,
            hasVariants = hasVariants,
            priceListTypeSelected = listPriceSelected,
            cashPriceTypeSelected = cashPriceSelected,
            percentageProfit = calculatePercentageProfitFromSellPrice(product.buyPrice, sellPrice = product.listPrice),
            priceProfit = calculatePriceFromPercentage(product.buyPrice, product.listPrice)
        ) as AddProductUiState
    }.catch { e -> }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddProductUiState.Loading)
    val uiState = _uiState

    val xx = calculatePercentageProfitFromSellPrice(_product.value.buyPrice, sellPrice = _product.value.listPrice)
    val xxx = calculatePriceFromPercentage(_product.value.buyPrice, _product.value.listPrice)


    fun tryAddProduct(onDone: (Boolean) -> Unit) {
        val state = _uiState.value as AddProductUiState.Success

        viewModelScope.launch {
            if (checkDataCorrect()) {
                val product = if (!state.product.manageStock) {
                    state.product.copy(currentStock = 0, adviceStock = 0)
                } else state.product

                async { addProduct(product) }.await()
                onDone(state.product.id != 0)
                cleanProductData()
                _events.emit("Operación exitosa!")
            } else {
                _events.emit("Faltan rellenar datos!")
            }
        }
    }

    fun deleteProduct(onDone: () -> Unit) {
        val state = _uiState.value as AddProductUiState.Success

        viewModelScope.launch {
            async { repository.deleteProductById(state.product.id) }.await()
            cleanProductData()
            _events.emit("Operación exitosa!")
            onDone()
        }

    }

    fun changeListPriceSelection(index: String) {
        val valueSelected = priceListType[index.toInt()]
        _listPriceSelected.update { valueSelected }
    }

    fun changeCashPriceSelection(index: String) {
        val valueSelected = cashPriceType[index.toInt()]
        _cashPriceSelected.update { valueSelected }
    }

    fun updateProduct(data: UpdatableProductData, value: Any) {

        when (data) {
            UpdatableProductData.NAME -> _product.update { current ->
                current.copy(name = value as String)
            }

            UpdatableProductData.CATEGORY -> _product.update { current ->
                current.copy(category = value as String)
            }

            UpdatableProductData.BRAND -> _product.update { current ->
                current.copy(brand = value as String)
            }

            UpdatableProductData.BUY_PRICE -> _product.update { current ->
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                current.copy(buyPrice = newValue)
            }

            UpdatableProductData.LIST_PRICE -> _product.update { current ->
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                current.copy(listPrice = newValue)
            }

            UpdatableProductData.CASH_PRICE -> _product.update { current ->
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                current.copy(cashPrice = newValue)
            }

            UpdatableProductData.CURRENT_STOCK -> _product.update { current ->
                val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                current.copy(currentStock = newValue)
            }

            UpdatableProductData.ADVICE_STOCK -> _product.update { current ->
                val newValue = if ((value as String).isBlank()) 0 else value.toInt()
                current.copy(adviceStock = newValue)
            }

            UpdatableProductData.DESCRIPTION -> _product.update { current ->
                current.copy(description = value as String)
            }

            UpdatableProductData.TOGGLE_HAS_VARIANTS -> _hasVariants.update { current ->
                !current
            }


            UpdatableProductData.TOGGLE_MANAGE_STOCK -> _product.update { current ->
                current.copy(manageStock = !current.manageStock)
            }

        }
    }

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            val product = async { repository.getProductById(id) }.await()
            _product.update { product }
        }
    }

    fun cleanProductData() {
        _product.update { CleanProduct().getCleanProduct() }
        _hasVariants.update { false }
    }
    fun checkDataCorrect(): Boolean {
        return _product.value.name.isNotBlank() &&
            _product.value.category.isNotBlank() &&
            _product.value.brand.isNotBlank() &&
            _product.value.buyPrice > 0 &&
            _product.value.listPrice > 0 &&
            _product.value.cashPrice > 0 &&
            _product.value.description.isNotBlank() &&
            if (_product.value.manageStock) _product.value.currentStock > 0 else _product.value.currentStock >= 0
    }
}

