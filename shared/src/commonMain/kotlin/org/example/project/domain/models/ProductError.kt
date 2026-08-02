package org.example.project.domain.models

sealed class ProductError: Exception() {
    data object ListPriceLessThanBuyPrice: ProductError()
    data object CashPriceMoreThenListPrice: ProductError()
    data object CashPriceLessThanBuyPrice: ProductError()
    data object NotEnoughData: ProductError()
    data object NoCashPriceData: ProductError()
    data object NoListPriceData: ProductError()
    data object NoBuyPriceData: ProductError()
    data object InvalidStockData: ProductError()


}