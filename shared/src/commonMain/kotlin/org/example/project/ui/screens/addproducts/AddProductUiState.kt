package org.example.project.ui.screens.addproducts

import org.example.project.domain.models.Product

sealed class AddProductUiState {
    data class Success(
        val product: Product = Product(
            name = "",
            category = "",
            brand = "",
            buyPrice = 0,
            listPrice = 0,
            cashPrice = 0,
            currentStock = 0,
            adviceStock = 0,
            description = "",
            manageStock = true,
        ),
        val hasVariants: Boolean = false,
    ) : AddProductUiState()

    data class Error(val msg: Throwable) : AddProductUiState()
    data object Loading : AddProductUiState()
}
