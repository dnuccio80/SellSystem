package org.example.project.domain.models.sell

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.SellEntity

data class Sell(
    val id:Int = 0,
    val date: LocalDate? = null,
    val isUsualClient: Boolean = false,
    val clientName:String,
    val description: String, // items, quantities, discounts
    val paymentMethod: String,
    val total:Long,
) {
    fun toEntity(): SellEntity {
        return SellEntity(
            id = id,
            date = date!!,
            clientName = clientName.ifBlank { null },
            description = description,
            paymentMethod = paymentMethod,
            total = total
        )
    }
}
