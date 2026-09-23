package org.example.project.ui.screens.newsell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.sell.SellError
import org.example.project.domain.usecases.clients.GetClients
import org.example.project.domain.usecases.newsell.GetSubtotalProductWithQuantity
import org.example.project.domain.usecases.newsell.GetTotalAmountSell
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.domain.usecases.products.GetProducts
import org.example.project.domain.usecases.sells.CreateNewSell
import org.example.project.ui.models.ProductWithQuantity
import org.example.project.ui.models.SellPresentation
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NewSellViewModel(
    private val getProducts: GetProducts,
    getClients: GetClients,
    private val getSubtotalProductWithQuantity: GetSubtotalProductWithQuantity,
    private val getTotalAmountSell: GetTotalAmountSell,
    private val createNewSell: CreateNewSell,
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

    private val _isUsualClient = MutableStateFlow(false)
    val isUsualClient = _isUsualClient.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

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

    fun updateClientSelected(newValue: String) {
        _clientSelected.update { newValue }
    }

    fun updateIsUsualClient(newValue: Boolean) {
        _isUsualClient.update { newValue }
    }

    fun toggleIsUsualClient() {
        _isUsualClient.update { !it }
    }

    fun addProductToCart(list: List<Product>) {

        val newList = mutableListOf<ProductWithQuantity>()

        _productWithQuantityList.value.forEach {
            newList.add(it)
        }
        list.forEach { product ->
            val productWithQuantity = ProductWithQuantity(
                product = product,
                quantity = 1,
                amount = getSubtotalProductWithQuantity(product, 1),
            )
            if (newList.none { it.product.id == product.id }) {
                newList.add(productWithQuantity)
            }
        }
        _productWithQuantityList.update { newList }
    }

    fun increaseProductQuantity(productId: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if (productWithQuantity.product.id == productId) {

                    val newQuantity = productWithQuantity.quantity + 1
                    val newAmount =
                        getSubtotalProductWithQuantity(productWithQuantity.product, newQuantity)

                    productWithQuantity.copy(quantity = newQuantity, amount = newAmount)
                } else productWithQuantity
            }
        }
    }

    fun manualQuantityChange(productId: Int, value: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if (productWithQuantity.product.id == productId) {
                    val newAmount =
                        getSubtotalProductWithQuantity(productWithQuantity.product, value)
                    productWithQuantity.copy(quantity = value, amount = newAmount)
                } else productWithQuantity
            }
        }
    }

    fun updatePaymentMethod(newValue: PaymentMethod) {

        when {
            newValue == PaymentMethod.CURRENT_ACCOUNT && _paymentMethod.value != PaymentMethod.CURRENT_ACCOUNT -> {
                _isUsualClient.value = false
                clearClientSelected()
            }

            newValue != PaymentMethod.CURRENT_ACCOUNT && _paymentMethod.value == PaymentMethod.CURRENT_ACCOUNT -> {
                _isUsualClient.value = false
                clearClientSelected()
            }
        }

        _paymentMethod.update { newValue }

    }

    fun updateClientSearchQuery(newValue: String) {
        _clientSearchQuery.update { newValue }
    }

    fun decreaseProductQuantity(productId: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if (productWithQuantity.product.id == productId) {
                    val newQuantity = productWithQuantity.quantity - 1
                    val newAmount =
                        getSubtotalProductWithQuantity(productWithQuantity.product, newQuantity)
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

    fun addSell(onDone: () -> Unit) {
        val newSell = SellPresentation(
            isUsualClient = _isUsualClient.value,
            clientName = clientSelected.value,
            productQuantityList = _productWithQuantityList.value,
            paymentMethod = _paymentMethod.value,
            totalAmount = getTotalAmountSell(
                _paymentMethod.value,
                _productWithQuantityList.value
            ).total
        )
        viewModelScope.launch {
            try {
                createNewSell(newSell)
                onDone()
                clearSellData()
            } catch (e: SellError) {
                _events.emit(e.msg)
            }
        }

    }

    fun clearClientSelected() {
        _clientSelected.update { "" }
    }

    fun clearSellData() {
        clearClientSelected()
        _paymentMethod.update { PaymentMethod.CASH }
        _isUsualClient.update { false }
        _productWithQuantityList.update { emptyList() }
    }


}