package org.example.project.ui.screens.newsell

import org.example.project.domain.models.client.Client
import org.example.project.domain.models.product.Product
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.domain.usecases.newsell.SellAmounts
import org.example.project.ui.models.ProductWithQuantity

sealed class NewSellUiState {
    data object Loading: NewSellUiState()
    data class Error(val throwable: Throwable): NewSellUiState()
    data class Success(
        val productList:List<Product>,
        val productQuery:String,
        val productWithQuantityList:List<ProductWithQuantity>,
        val amounts: SellAmounts,
        val paymentMethod: PaymentMethod,
        val clients:List<Client>
    ): NewSellUiState()
}
