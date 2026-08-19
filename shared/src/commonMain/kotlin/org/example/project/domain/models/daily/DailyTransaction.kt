package org.example.project.domain.models.daily

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.DailyTransactionEntity

data class DailyTransaction(
    val id: Int = 0,
    val description: String,
    val amount: Long,
    val type: TransactionType,
    val date: LocalDate,
) {
    fun toEntity(): DailyTransactionEntity{
        return DailyTransactionEntity(
            id = id,
            description = description,
            amount = amount,
            type = type,
            date = date
        )
    }
}
