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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.client.Client
import org.example.project.domain.models.product.ProductWithPromotion
import org.example.project.domain.models.sell.SellError
import org.example.project.domain.usecases.clients.GetClients
import org.example.project.domain.usecases.newsell.GetSubtotalProductWithQuantity
import org.example.project.domain.usecases.newsell.GetTotalAmountSell
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.domain.usecases.products.GetProductsWithPromotion
import org.example.project.domain.usecases.sells.CreateNewSell
import org.example.project.ui.models.ProductWithQuantityPresentation
import org.example.project.ui.models.SellPresentation
import org.example.project.ui.screens.clients.CleanClient
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds

data class ProductsFlow(
    val productList: List<ProductWithPromotion>,
    val productWithQuantityList :List<ProductWithQuantityPresentation>
)

data class PromoCounts(
    val countPromoDiscount: Boolean,
    val countPaymentDiscount: Boolean,
)

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NewSellViewModel(
    private val getProductsWithPromotion: GetProductsWithPromotion,
    getClients: GetClients,
    private val getSubtotalProductWithQuantity: GetSubtotalProductWithQuantity,
    private val getTotalAmountSell: GetTotalAmountSell,
    private val createNewSell: CreateNewSell,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _clientSearchQuery = MutableStateFlow("")
    val clientSearchQuery = _clientSearchQuery.asStateFlow()

    private val _paymentMethod = MutableStateFlow(PaymentMethod.CASH)
    private val _clients = _clientSearchQuery.debounce(300.milliseconds).flatMapLatest { query ->
        getClients(query).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
    private val _productList: StateFlow<List<ProductWithPromotion>> = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getProductsWithPromotion(query).map { it.filter { productWithPromo -> !productWithPromo.product.manageStock || productWithPromo.product.currentStock > 0 } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _productWithQuantityList =
        MutableStateFlow<List<ProductWithQuantityPresentation>>(emptyList())

    private val _productsFlow = combine(
        _productList,
        _productWithQuantityList
    ) { productList, productWithQuantityList ->
        ProductsFlow(
            productList = productList,
            productWithQuantityList = productWithQuantityList
        )
    }

    private val _clientSelected = MutableStateFlow<Client?>(null)
    val clientSelected = _clientSelected.asStateFlow()

    private val _isUsualClient = MutableStateFlow(false)
    val isUsualClient = _isUsualClient.asStateFlow()

    private val _countPromoDiscount = MutableStateFlow(true)
    val countPromoDiscount = _countPromoDiscount.asStateFlow()

    private val _countPaymentDiscount = MutableStateFlow(true)
    val countPaymentDiscount = _countPaymentDiscount.asStateFlow()

    private val _promoCounts = combine(
        _countPromoDiscount,
        _countPaymentDiscount
    ) { countPromo, countPayment ->
        PromoCounts(
            countPromoDiscount = countPromo,
            countPaymentDiscount = countPayment
        )
    }


    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()


    private val _uiState: StateFlow<NewSellUiState> = combine(
        _query,
        _productsFlow,
        _paymentMethod,
        _promoCounts,
        _clients
    ) { query, productsFlow, paymentMethod, promoCounts, clients ->
        NewSellUiState.Success(
            productList = productsFlow.productList,
            productQuery = query,
            productWithQuantityList = productsFlow.productWithQuantityList,
            amounts = getTotalAmountSell(
                paymentMethod = paymentMethod,
                productWithQuantityList = productsFlow.productWithQuantityList,
                countPaymentDiscount = promoCounts.countPaymentDiscount,
                countPromoDiscount = promoCounts.countPromoDiscount
            ),
            paymentMethod = paymentMethod,
            clients = clients
        ) as NewSellUiState
    }.catch { e -> NewSellUiState.Error(e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NewSellUiState.Loading)
    val uiState = _uiState

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }

    fun updateClientSelected(newValue: Client) {
        _clientSelected.update { newValue }
    }

    fun toggleIsUsualClient() {
        _isUsualClient.update { !it }
    }

    fun addProductToCart(list: List<ProductWithPromotion>) {

        val newList = mutableListOf<ProductWithQuantityPresentation>()

        _productWithQuantityList.value.forEach {
            newList.add(it)
        }
        list.forEach { productWithPromo ->
            val productWithQuantity = ProductWithQuantityPresentation(
                productWithPromo = productWithPromo,
                quantity = 1,
                amount = getSubtotalProductWithQuantity(productWithPromo, 1),
            )
            if (newList.none { it.productWithPromo.product.id == productWithPromo.product.id }) {
                newList.add(productWithQuantity)
            }
        }
        _productWithQuantityList.update { newList }
    }

    fun increaseProductQuantity(productId: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if (productWithQuantity.productWithPromo.product.id == productId) {
                    val newQuantity = (productWithQuantity.quantity + 1)
                    if (productWithQuantity.productWithPromo.product.manageStock) newQuantity.coerceAtMost(
                        productWithQuantity.productWithPromo.product.currentStock
                    )
                    val newAmount =
                        getSubtotalProductWithQuantity(
                            productWithQuantity.productWithPromo,
                            newQuantity
                        )
                    productWithQuantity.copy(quantity = newQuantity, amount = newAmount)
                } else productWithQuantity
            }
        }
    }

    fun manualQuantityChange(productId: Int, value: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if (productWithQuantity.productWithPromo.product.id == productId) {
                    val newAmount =
                        getSubtotalProductWithQuantity(productWithQuantity.productWithPromo, value)
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

    fun toggleCountPromoDiscount() {
        _countPromoDiscount.update { !it }
    }

    fun toggleCountPaymentDiscount() {
        _countPaymentDiscount.update { !it }
    }

    fun decreaseProductQuantity(productId: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.map { productWithQuantity ->
                if (productWithQuantity.productWithPromo.product.id == productId) {
                    val newQuantity = productWithQuantity.quantity - 1
                    val newAmount =
                        getSubtotalProductWithQuantity(
                            productWithQuantity.productWithPromo,
                            newQuantity
                        )
                    productWithQuantity.copy(quantity = newQuantity, amount = newAmount)
                } else productWithQuantity
            }
        }
    }

    fun deleteProductWithQuantity(productId: Int) {
        _productWithQuantityList.update { currentList ->
            currentList.filterNot { it.productWithPromo.product.id == productId }
        }
    }

    fun addSell(onDone: () -> Unit) {
        val newSell = SellPresentation(
            isUsualClient = _isUsualClient.value,
            client = clientSelected.value ?: CleanClient().getCleanClient(),
            productQuantityList = _productWithQuantityList.value,
            paymentMethod = _paymentMethod.value,
            totalAmount = getTotalAmountSell(
                _paymentMethod.value,
                _productWithQuantityList.value,
                countPaymentDiscount = _countPaymentDiscount.value,
                countPromoDiscount = _countPromoDiscount.value
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
        _clientSelected.update { null }
    }

    fun clearSellData() {
        clearClientSelected()
        _paymentMethod.update { PaymentMethod.CASH }
        _isUsualClient.update { false }
        _productWithQuantityList.update { emptyList() }
        _countPromoDiscount.update { true }
        _countPaymentDiscount.update { true }
    }


}