package org.example.project.ui.screens.addproducts

import org.example.project.domain.models.product.ProductWithPromo
import org.example.project.domain.models.product.ProductCategory

sealed class AddProductUiState {
    data class Success(
        val priceListTypeSelected: String,
        val cashPriceTypeSelected:String,
        val product: ProductWithPromo,
        val hasVariants: Boolean = false,
        val percentageListProfit: Long,
        val percentageCashDiscount:Long,
        val categories:List<ProductCategory>
    ) : AddProductUiState()

    data class Error(val msg: Throwable) : AddProductUiState()
    data object Loading : AddProductUiState()
}
