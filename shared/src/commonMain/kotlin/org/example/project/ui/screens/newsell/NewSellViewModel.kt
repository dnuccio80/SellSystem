package org.example.project.ui.screens.newsell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.project.domain.models.product.Product
import org.example.project.domain.usecases.clients.GetClients
import org.example.project.domain.usecases.newsell.GetSubtotalProductWithQuantity
import org.example.project.domain.usecases.newsell.GetTotalAmountSell
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.domain.usecases.products.GetProducts
import org.example.project.ui.models.ProductWithQuantity
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NewSellViewModel(
    private val getProducts: GetProducts,
    getClients: GetClients,
    private val getSubtotalProductWithQuantity: GetSubtotalProductWithQuantity,
    private val getTotalAmountSell: GetTotalAmountSell
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _clientSearchQuery = MutableStateFlow("")
    val clientSearchQuery = _clientSearchQuery.asStateFlow()

    private val _paymentMethod = MutableStateFlow<PaymentMethod>(PaymentMethod.CASH)
    private val _clients = _clientSearchQuery.debounce(300.milliseconds).flatMapLatest { query ->
        getClients(query).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
    private val _productList = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getProducts(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _productWithQuantityList = MutableStateFlow<List<ProductWithQuantity>>(emptyList())

    private val _clientSelected = MutableStateFlow<String>("")
    val clientSelected = _clientSelected.asStateFlow()

    private val _uiState: StateFlow<NewSellUiState> = combine(
        _query,
        _productList,
        _productWithQuantityList,
        _paymentMethod,
        _clients
    ) { query, productsList, productWithQuantityList, paymentMethod, clients ->
        NewSellUiState.Success(
            productList = productsList,
            productQuery = query,
            productWithQuantityList = productWithQuantityList,
            amounts = getTotalAmountSell(paymentMethod, productWithQuantityList),
            paymentMethod = paymentMethod,
            clients = clients
        ) as NewSellUiState
    }.catch { e -> NewSellUiState.Error(e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NewSellUiState.Loading)
    val uiState = _uiState

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }

    fun updateClientSelected(newValue:String) {
        _clientSelected.update { newValue }
    }

    fun addProductToCart(list: List<Product>) {

        val newList = mutableListOf<ProductWithQuantity>()

        _productWithQuantityList.value.forEach {
            newList.add(it)
        }
        list.forEach {product ->
            val productWithQuantity = ProductWithQuantity(
                product = product,
                quantity = 1,
                amount = getSubtotalProductWithQuantity(product, 1),
            )
            if(newList.none { it.product.id == product.id}) {
                newList.add(productWithQuantity)
            }
        }
        _productWithQuantityList.update { newList }
    }

    fun increaseProductQuantity(productId: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if(productWithQuantity.product.id == productId) {

                    val newQuantity = productWithQuantity.quantity + 1
                    val newAmount = getSubtotalProductWithQuantity(productWithQuantity.product, newQuantity)

                    productWithQuantity.copy(quantity = newQuantity, amount = newAmount)
                } else productWithQuantity
            }
        }
    }

    fun manualQuantityChange(productId:Int, value:Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if(productWithQuantity.product.id == productId) {
                    val newAmount = getSubtotalProductWithQuantity(productWithQuantity.product, value)
                    productWithQuantity.copy(quantity = value, amount = newAmount)
                } else productWithQuantity
            }
        }
    }

    fun updatePaymentMethod(newValue: PaymentMethod) {
        _paymentMethod.update { newValue }
    }

    fun updateClientSearchQuery(newValue: String) {
        _clientSearchQuery.update { newValue }
    }

    fun decreaseProductQuantity(productId:Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if(productWithQuantity.product.id == productId) {
                    val newQuantity = productWithQuantity.quantity - 1
                    val newAmount = getSubtotalProductWithQuantity(productWithQuantity.product, newQuantity)
                    productWithQuantity.copy(quantity = newQuantity, amount = newAmount)
                } else productWithQuantity
            }
        }
    }

    fun deleteProductWithQuantity(productId: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.filterNot { it.product.id == productId }
        }
    }


}