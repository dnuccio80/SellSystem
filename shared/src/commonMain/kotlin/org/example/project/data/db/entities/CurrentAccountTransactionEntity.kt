package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction

enum class TransactionType(val etiquette:String) {
    PURCHASE("Compra"), PAYMENT("Pago"), NONE("")
}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("clientId")]
)
data class CurrentAccountTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val clientId:Int,
    val description:String,
    val amount:Long,
    val type: TransactionType,
    val date: LocalDate
) {
    fun toDomain(): CurrentAccountTransaction {
        return CurrentAccountTransaction(
            id = id,
            clientId = clientId,
            description = description,
            amount = amount,
            type = type,
            date = date
        )
    }
}
