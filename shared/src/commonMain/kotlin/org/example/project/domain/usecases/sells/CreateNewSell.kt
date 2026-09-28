package org.example.project.domain.usecases.sells

import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction
import org.example.project.domain.repositories.SellRepository
import org.example.project.domain.models.sell.SellError
import org.example.project.domain.repositories.CurrentAccountDetailRepository
import org.example.project.domain.repositories.CurrentAccountRepository
import org.example.project.domain.repositories.ProductRepository
import org.example.project.domain.usecases.utils.GetCurrentDate
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.ui.ext.formatToDisplay
import org.example.project.ui.models.SellPresentation

class CreateNewSell(
    private val sellRepository: SellRepository,
    private val productRepository: ProductRepository,
    private val currentAccountDetailRepository: CurrentAccountDetailRepository,
    private val getCurrentDate: GetCurrentDate,
) {

    suspend operator fun invoke(sellPresentation: SellPresentation) {
        when {
            sellPresentation.isUsualClient && sellPresentation.client.fullName.isBlank() -> throw SellError.UsualClientButNotSelected
            sellPresentation.productQuantityList.isEmpty() -> throw SellError.NoItems
            sellPresentation.paymentMethod == PaymentMethod.CURRENT_ACCOUNT && sellPresentation.client.fullName.isBlank() -> throw SellError.CurrentAccountButNotSelected
        }

        val sell = sellPresentation.toDomain()
        val products = sellPresentation.productQuantityList.map { it.toDomain() }
        val today = getCurrentDate()


        try {
            if (sellPresentation.paymentMethod == PaymentMethod.CURRENT_ACCOUNT) {
                val transaction = CurrentAccountTransaction(
                    id = 0,
                    clientId = sellPresentation.client.id,
                    description = "Compra a cuenta corriente fecha: ${today.formatToDisplay()}",
                    amount = sell.total,
                    type = TransactionType.PURCHASE,
                    date = today
                )
                val transactionId = currentAccountDetailRepository.addTransaction(transaction)
                sellRepository.addSellWithProducts(sell.copy(date = today, transactionId = transactionId), products)
            } else {
                sellRepository.addSellWithProducts(sell.copy(date = today), products)
            }
            sellPresentation.productQuantityList.forEach { productWithQuantity ->
                if (productWithQuantity.product.manageStock) {
                    val updatedStock =
                        productWithQuantity.product.currentStock - productWithQuantity.quantity
                    productRepository.updateProduct(productWithQuantity.product.copy(currentStock = updatedStock))
                }
            }
        } catch (e: Throwable) {
            throw e
        }

    }

}