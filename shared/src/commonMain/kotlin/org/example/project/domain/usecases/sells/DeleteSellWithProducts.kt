package org.example.project.domain.usecases.sells

import org.example.project.domain.repositories.CurrentAccountDetailRepository
import org.example.project.domain.repositories.ProductRepository
import org.example.project.domain.repositories.SellRepository

class DeleteSellWithProducts(
    private val sellRepository: SellRepository,
    private val productRepository: ProductRepository,
    private val currentAccountDetailRepository: CurrentAccountDetailRepository
) {
    suspend operator fun invoke(sellId: Int) {

        val productsInSell = sellRepository.getProductsFromSell(sellId)

        productsInSell.forEach { productWithQuantity ->
            if (productWithQuantity.product.manageStock) {
                val newStock =
                    productWithQuantity.product.currentStock + productWithQuantity.quantity
                productRepository.updateProduct(productWithQuantity.product.copy(currentStock = newStock))
            }
        }
        val sell = sellRepository.getSellById(sellId)
        if(sell.transactionId != 0L) {
            currentAccountDetailRepository.deleteTransaction(sell.transactionId.toInt())
        }
        sellRepository.deleteSellById(sellId)

    }
}