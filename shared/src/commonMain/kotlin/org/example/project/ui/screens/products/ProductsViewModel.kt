package org.example.project.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.example.project.domain.usecases.products.GetProducts
import kotlin.time.Duration.Companion.milliseconds

class ProductsViewModel(
    getProducts: GetProducts,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _products = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getProducts(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val products = _products

    fun updateQuery(newValue: String) {
        _query.value = newValue
    }


}