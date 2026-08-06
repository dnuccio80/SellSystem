package org.example.project.ui.screens.products

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.project.domain.models.Product
import org.example.project.domain.models.ProductCategory

sealed class ProductUiState {
    data object Loading: ProductUiState()
    data class Success(
        val products: List<Product>,
        val categories:List<ProductCategory>,
        val newCategory: ProductCategory

    ): ProductUiState()
    data class Error(val throwable: Throwable): ProductUiState()
}