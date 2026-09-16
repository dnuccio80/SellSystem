package org.example.project.ui.models

import org.example.project.domain.models.sell.Sell
import org.example.project.domain.usecases.newsell.PaymentMethod

data class SellPresentation(
    val id:Int = 0,
    val isUsualClient: Boolean,
    val clientName: String,
    val productQuantityList:List<ProductWithQuantity>,
    val paymentMethod: PaymentMethod,
    val totalAmount:Long
) {
    fun toDomain(): Sell {
        return Sell(
            id = id,
            clientName = clientName,
            description = getDescription(productQuantityList),
            paymentMethod = paymentMethod.etiquette,
            total = totalAmount,
        )
    }
}

private fun getDescription(productQuantityList: List<ProductWithQuantity>): String {
    val description = StringBuilder()
    productQuantityList.forEach { productWithQuantity ->
        description.append("${productWithQuantity.product} x ${productWithQuantity.quantity} \n")
    }
    return description.toString()
}