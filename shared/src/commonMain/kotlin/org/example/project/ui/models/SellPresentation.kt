package org.example.project.ui.models

import org.example.project.domain.models.client.Client
import org.example.project.domain.models.sell.Sell
import org.example.project.domain.usecases.newsell.PaymentMethod

data class SellPresentation(
    val id:Int = 0,
    val isUsualClient: Boolean,
    val client: Client,
    val productQuantityList:List<ProductWithQuantityPresentation>,
    val paymentMethod: PaymentMethod,
    val totalAmount:Long
) {
    fun toDomain(): Sell {
        return Sell(
            id = id,
            clientName = client.fullName,
            paymentMethod = paymentMethod.etiquette,
            total = totalAmount,
        )
    }

}

