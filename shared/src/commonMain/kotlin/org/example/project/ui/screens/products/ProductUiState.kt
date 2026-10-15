package org.example.project.ui.screens.products

import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.product.ProductWithPromotion

sealed class ProductUiState {
    data object Loading: ProductUiState()
    data class Success(
        val products: List<ProductWithPromotion>,
        val categories:List<ProductCategory>,
        val newCategory: ProductCategory

    ): ProductUiState()
    data class Error(val throwable: Throwable): ProductUiState()
}