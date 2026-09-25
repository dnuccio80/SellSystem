package org.example.project.ui.models

import org.example.project.data.db.entities.SellEntity
import org.example.project.domain.models.sell.ProductWithQuantity
import org.example.project.domain.models.sell.Sell
import org.example.project.domain.usecases.newsell.PaymentMethod

data class SellPresentation(
    val id:Int = 0,
    val isUsualClient: Boolean,
    val clientName: String,
    val productQuantityList:List<ProductWithQuantityPresentation>,
    val paymentMethod: PaymentMethod,
    val totalAmount:Long
) {
    fun toDomain(): Sell {
        return Sell(
            id = id,
            clientName = clientName,
            paymentMethod = paymentMethod.etiquette,
            total = totalAmount,
        )
    }

}

