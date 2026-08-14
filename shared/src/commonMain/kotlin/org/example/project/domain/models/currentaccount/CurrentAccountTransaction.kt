package org.example.project.domain.models.currentaccount

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.CurrentAccountTransactionEntity
import org.example.project.data.db.entities.TransactionType

data class CurrentAccountTransaction(
    val id:Int,
    val clientId:Int,
    val description:String,
    val amount:Long,
    val type: TransactionType,
    val date: LocalDate
) {
    fun toEntity(): CurrentAccountTransactionEntity {
        return CurrentAccountTransactionEntity(
            id = id,
            clientId = clientId,
            description = description,
            amount = amount,
            type = type,
            date = date
        )
    }
}

class CleanCurrentAccountTransaction {
    fun getCleanCurrentAccountTransaction(): CurrentAccountTransaction {
        return CurrentAccountTransaction(
            id = 0,
            clientId = 0,
            description = "",
            amount = 0L,
            type = TransactionType.NONE,
            date = LocalDate.fromEpochDays(111)
        )
    }
}

