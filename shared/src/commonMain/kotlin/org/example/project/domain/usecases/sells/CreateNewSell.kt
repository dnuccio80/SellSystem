package org.example.project.domain.usecases.sells

import org.example.project.domain.repositories.SellRepository
import org.example.project.domain.models.sell.SellError
import org.example.project.domain.repositories.ProductRepository
import org.example.project.domain.usecases.utils.GetCurrentDate
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.ui.models.SellPresentation

class CreateNewSell(private val sellRepository: SellRepository, private val productRepository: ProductRepository, private val getCurrentDate: GetCurrentDate) {

    suspend operator fun invoke(sellPresentation: SellPresentation) {
        when {
            sellPresentation.isUsualClient && sellPresentation.clientName.isBlank() -> throw SellError.UsualClientButNotSelected
            sellPresentation.productQuantityList.isEmpty() -> throw SellError.NoItems
            sellPresentation.paymentMethod == PaymentMethod.CURRENT_ACCOUNT && sellPresentation.clientName.isBlank() ->  throw SellError.CurrentAccountButNotSelected
        }

        val sell = sellPresentation.toDomain()
        val products = sellPresentation.productQuantityList.map { it.toDomain() }
        val today = getCurrentDate()

        try {
            sellRepository.addSellWithProducts(sell.copy(date = today), products)
            sellPresentation.productQuantityList.forEach { productWithQuantity ->
                if(productWithQuantity.product.manageStock) {
                    val updatedStock = productWithQuantity.product.currentStock - productWithQuantity.quantity
                    productRepository.updateProduct(productWithQuantity.product.copy(currentStock = updatedStock))
                }
            }
        }catch (e: Throwable) {
            throw e
        }

    }

}