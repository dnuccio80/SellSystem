package org.example.project.domain.models.currentaccount

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.daily.DailyTransaction

@Entity
data class DailyTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val amount: Long,
    val type: TransactionType,
    val date: LocalDate,
) {
    fun toDomain(): DailyTransaction {
        return DailyTransaction(
            id = id,
            description = description,
            amount = amount,
            type = type,
            date = date
        )
    }
}
