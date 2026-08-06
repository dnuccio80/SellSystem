package org.example.project.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
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
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.product.ProductError
import org.example.project.domain.repositories.ProductCategoryRepository
import org.example.project.domain.usecases.products.AddCategory
import org.example.project.domain.usecases.products.GetCategories
import org.example.project.domain.usecases.products.GetProducts
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ProductsViewModel(
    getProducts: GetProducts,
    getCategories: GetCategories,
    private val addCategory: AddCategory,
    private val repository: ProductCategoryRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _products = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getProducts(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _categories = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getCategories(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _newCategory = MutableStateFlow(ProductCategory(name = ""))
    private val _uiState: StateFlow<ProductUiState> = combine(
        _products,
        _categories,
        _newCategory
    ) { products, categories, newCategory ->
        ProductUiState.Success(
            products = products,
            categories = categories,
            newCategory = newCategory
        )
    }.catch { e ->
        ProductUiState.Error(e)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductUiState.Loading)
    val uiState = _uiState

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun updateQuery(newValue: String) {
        _query.value = newValue
    }

    fun updateCategoryName(newValue: String) {
        _newCategory.update { it.copy(name = newValue) }
    }

    fun getCategory(id: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            val category = async { repository.getCategoryById(id) }.await()
            _newCategory.update { category }
            onDone()
        }
    }
    fun tryAddCategory(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                addCategory(_newCategory.value)
                onDone()
                cleanCategory()
            } catch (e: ProductError) {
                _events.emit("El nombre de la categoría no puede estar en blanco")
            }
        }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            repository.deleteCategory(_newCategory.value.id)
            cleanCategory()
        }
    }

    fun cleanCategory() {
        _newCategory.update { current -> current.copy(id = 0, name = "") }
    }
}