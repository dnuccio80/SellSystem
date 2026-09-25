package org.example.project.domain.usecases.sells

import org.example.project.domain.repositories.SellRepository
import org.example.project.ui.models.SellWithProductsWithQuantity

class GetSellData(private val sellRepository: SellRepository) {

    suspend operator fun invoke(sellId:Int): SellWithProductsWithQuantity {

        val sell = sellRepository.getSellById(sellId)
        val productsWithQuantity = sellRepository.getProductsFromSell(sellId)

        return SellWithProductsWithQuantity(
            sell = sell,
            productWithQuantity = productsWithQuantity
        )
    }

}