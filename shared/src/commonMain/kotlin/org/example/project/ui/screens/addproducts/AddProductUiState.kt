package org.example.project.ui.screens.addproducts

import org.example.project.domain.models.Product

sealed class AddProductUiState {
    data class Success(

        val priceListType: List<String> = listOf<String>(
            "Precio",
            "Porcentaje de ganancia"
        ),
        val cashPriceType: List<String> = listOf(
            "Precio",
            "Porcentaje de descuento"
        ),
        val priceListTypeSelected: String = priceListType.first(),

        val cashPriceTypeSelected:String = cashPriceType.first(),

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
