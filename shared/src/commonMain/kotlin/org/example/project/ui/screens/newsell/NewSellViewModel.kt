package org.example.project.ui.screens.newsell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.groupBy
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.project.domain.models.product.Product
import org.example.project.domain.usecases.products.GetProducts
import org.example.project.ui.models.ProductWithQuantity
import org.koin.core.qualifier._q
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds

class NewSellViewModel(
    private val getProducts: GetProducts,
) : ViewModel() {

    private val _query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _productList = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getProducts(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _productWithQuantityList = MutableStateFlow<List<ProductWithQuantity>>(emptyList())

    private val _uiState: StateFlow<NewSellUiState> = combine(
        _query,
        _productList,
        _productWithQuantityList
    ) { query, productsList, productWithQuantityList ->
        NewSellUiState.Success(
            productList = productsList,
            productQuery = query,
            productWithQuantityList = productWithQuantityList
        ) as NewSellUiState
    }.catch { e -> NewSellUiState.Error(e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NewSellUiState.Loading)
    val uiState = _uiState

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }

    fun addProductToCart(list: List<Product>) {

        val newList = mutableListOf<ProductWithQuantity>()

        _productWithQuantityList.value.forEach {
            newList.add(it)
        }
        list.forEach {
            val productWithQuantity = ProductWithQuantity(
                product = it,
                quantity = 1
            )
            newList.add(productWithQuantity)
        }

        _productWithQuantityList.update { newList }

        print("LISTA: $list")
        print("NEW LISTA: $newList")
        print("UPDATED LISTA: ${_productWithQuantityList.value}")

    }
}