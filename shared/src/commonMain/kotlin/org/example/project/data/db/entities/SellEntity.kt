package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.project.domain.models.sell.Sell
import org.example.project.domain.usecases.newsell.PaymentMethod

@Entity
data class SellEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val date: LocalDate,
    val clientName:String?,
    val paymentMethod: String,
    val total:Long,
) {
    fun toDomain(): Sell {
        return Sell(
            id = id,
            date = date,
            clientName = clientName.orEmpty(),
            paymentMethod = paymentMethod,
            total = total
        )
    }
}
