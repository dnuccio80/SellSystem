package org.example.project.ui.screens.addproducts

import org.example.project.domain.models.Product

sealed class AddProductUiState {
    data class Success(
        val priceListTypeSelected: String,
        val cashPriceTypeSelected:String,
        val product: Product,
        val hasVariants: Boolean = false,
        val percentageListProfit:String,
        val priceListProfit:String,
//        val percentageCashProfit:String,
//        val priceCashProfit:String,
    ) : AddProductUiState()

    data class Error(val msg: Throwable) : AddProductUiState()
    data object Loading : AddProductUiState()
}
