package org.example.project.ui.screens.addproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.data.storage.ImagePicker
import org.example.project.data.storage.ImageStorage
import org.example.project.domain.models.PercentageValues
import org.example.project.domain.models.ProductError
import org.example.project.domain.models.ProductError.*
import org.example.project.domain.usecases.products.AddProduct
import org.example.project.domain.usecases.products.CalculatePercentageDiscountFromCashPrice
import org.example.project.domain.usecases.products.CalculatePercentageProfitFromSellPrice
import org.example.project.domain.usecases.products.CalculatePriceFromPercentageAdd
import org.example.project.domain.usecases.products.CalculatePriceFromPercentageDiscount
import org.example.project.ui.screens.products.CleanProduct

enum class UpdatableProductData {
    NAME, CATEGORY, BRAND, BUY_PRICE, LIST_PRICE, LIST_PRICE_PERCENTAGE, CASH_PRICE, CASH_PRICE_PERCENTAGE, CURRENT_STOCK, ADVICE_STOCK, DESCRIPTION, TOGGLE_HAS_VARIANTS, TOGGLE_MANAGE_EXPIRE_DATE, EXPIRE_DATE, TOGGLE_MANAGE_STOCK, DELETE_IMAGE
}

sealed class PriceListType(val name: String) {
    data object Price : PriceListType("Precio")
    data object PercentEarn : PriceListType("Porcentaje de ganancia")
}

sealed class PriceCashType(val name: String) {
    data object Price : PriceCashType("Precio")
    data object PercentDiscount : PriceCashType("Porcentaje de descuento")
}

class AddProductViewModel(
    private val addProduct: AddProduct,
    private val repository: ProductRepositoryImpl,
    private val calculatePercentageProfitFromSellPrice: CalculatePercentageProfitFromSellPrice,
    private val calculatePriceFromPercentage: CalculatePriceFromPercentageAdd,
    private val calculatePriceFromPercentageDiscount: CalculatePriceFromPercentageDiscount,
    private val calculatePercentageDiscountFromCashPrice: CalculatePercentageDiscountFromCashPrice,
    private val imageStorage: ImageStorage,
    private val imagePicker: ImagePicker,
    ) : ViewModel() {

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    val priceListType: List<String> = listOf(
        PriceListType.Price.name,
        PriceListType.PercentEarn.name
    )
    val cashPriceType: List<String> = listOf(
        PriceCashType.Price.name,
        PriceCashType.PercentDiscount.name
    )
    private val _product = MutableStateFlow(CleanProduct().getCleanProduct())
    private val _hasVariants = MutableStateFlow(false)
    private val _listPriceSelected = MutableStateFlow(priceListType.first())
    private val _cashPriceSelected = MutableStateFlow(cashPriceType.first())
    private val _percentages = MutableStateFlow(PercentageValues())

    private val _uiState = combine(
        _product,
        _hasVariants,
        _listPriceSelected,
        _cashPriceSelected,
        _percentages
    ) { product, hasVariants, listPriceSelected, cashPriceSelected, percentages ->

        AddProductUiState.Success(
            product = product,
            hasVariants = hasVariants,
            priceListTypeSelected = listPriceSelected,
            cashPriceTypeSelected = cashPriceSelected,
            percentageListProfit = if (percentages.priceListAdd != 0L) percentages.priceListAdd else calculatePercentageProfitFromSellPrice(
                product.buyPrice,
                sellPrice = product.listPrice
            ),
            percentageCashDiscount = if (percentages.priceCashDiscount != 0L) percentages.priceCashDiscount else calculatePercentageDiscountFromCashPrice(
                listPrice = product.listPrice,
                cashPrice = product.cashPrice
            ),
        ) as AddProductUiState
    }.catch { e -> }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddProductUiState.Loading)
    val uiState = _uiState

    fun tryAddProduct(onDone: (Boolean) -> Unit) {

        viewModelScope.launch {
            try {
                async { addProduct(_product.value) }.await()
                onDone(_product.value.id != 0)
                cleanProductData()
                _events.emit("Operación exitosa!")
            } catch (e: ProductError) {
                _events.emit(e.msg)
            }
        }
    }

    fun selectImage() {
        viewModelScope.launch {
            val image = imagePicker.pickImage() ?: return@launch

            val path = imageStorage.saveImage(
                image = image.bytes,
                extension = image.extension
            )
            _product.update { current ->
                current.copy(imagePath = path)
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

                when (_listPriceSelected.value) {
                    PriceListType.PercentEarn.name
                        if _cashPriceSelected.value == PriceCashType.PercentDiscount.name -> {

                        val newListPrice = calculatePriceFromPercentage(
                            buyPrice = newValue,
                            percentage = _percentages.value.priceListAdd
                        )

                        current.copy(
                            buyPrice = newValue,
                            listPrice = newListPrice,
                            cashPrice = calculatePriceFromPercentageDiscount(
                                listPrice = newListPrice,
                                discount = _percentages.value.priceCashDiscount
                            )
                        )
                    }

                    PriceListType.PercentEarn.name if _cashPriceSelected.value != PriceCashType.PercentDiscount.name -> {
                        current.copy(
                            buyPrice = newValue,
                            listPrice = calculatePriceFromPercentage(
                                buyPrice = newValue,
                                percentage = _percentages.value.priceListAdd
                            )
                        )
                    }

                    else -> {
                        current.copy(buyPrice = newValue)
                    }
                }
            }

            UpdatableProductData.LIST_PRICE -> _product.update { current ->
                val newListPrice = if ((value as String).isBlank()) 0L else value.toLong()

                when {
                    _cashPriceSelected.value == PriceCashType.PercentDiscount.name -> {
                        current.copy(
                            listPrice = newListPrice,
                            cashPrice = calculatePriceFromPercentageDiscount(
                                listPrice = newListPrice,
                                discount = _percentages.value.priceCashDiscount
                            )
                        )
                    }

                    else -> {
                        current.copy(listPrice = newListPrice)
                    }
                }


            }

            UpdatableProductData.LIST_PRICE_PERCENTAGE -> {
                val percentage =
                    if ((value as String).isBlank()) 0L else value.toLong().coerceIn(0, 99)

                _percentages.update { current ->
                    current.copy(priceListAdd = percentage)
                }

                when {
                    _cashPriceSelected.value == PriceCashType.PercentDiscount.name -> {
                        _product.update { current ->

                            val newListPrice = calculatePriceFromPercentage(
                                buyPrice = current.buyPrice,
                                percentage = percentage
                            )
                            current.copy(
                                listPrice = newListPrice,
                                cashPrice = calculatePriceFromPercentageDiscount(
                                    listPrice = newListPrice,
                                    discount = _percentages.value.priceCashDiscount
                                )
                            )
                        }
                    }

                    else -> {
                        _product.update { current ->
                            current.copy(
                                listPrice = calculatePriceFromPercentage(
                                    buyPrice = current.buyPrice,
                                    percentage = percentage
                                ),
                            )
                        }
                    }
                }
            }

            UpdatableProductData.CASH_PRICE -> _product.update { current ->
                val newValue = if ((value as String).isBlank()) 0L else value.toLong()
                current.copy(cashPrice = newValue)
            }

            UpdatableProductData.CASH_PRICE_PERCENTAGE -> {
                val discount =
                    if ((value as String).isBlank()) 0L else value.toLong().coerceIn(0, 99)

                _percentages.update { current ->
                    current.copy(priceCashDiscount = discount)
                }
                _product.update { current ->
                    current.copy(
                        cashPrice = calculatePriceFromPercentageDiscount(
                            listPrice = current.listPrice,
                            discount = discount
                        )
                    )
                }

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

            UpdatableProductData.TOGGLE_MANAGE_EXPIRE_DATE -> _product.update { current ->
                current.copy(manageExpireDate = !current.manageExpireDate)
            }

            UpdatableProductData.EXPIRE_DATE -> _product.update { current ->
                current.copy(expireDate = value as LocalDate)
            }

            UpdatableProductData.DELETE_IMAGE -> _product.update { current ->
                current.copy(imagePath = null)
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
        _percentages.update { current ->
            current.copy(priceListAdd = 0L, priceCashDiscount = 0L)
        }
        _hasVariants.update { false }
    }

}

