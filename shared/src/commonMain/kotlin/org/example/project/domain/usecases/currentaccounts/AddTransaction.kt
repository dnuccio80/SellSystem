package org.example.project.domain.usecases.currentaccounts

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction
import org.example.project.domain.models.currentaccount.CurrentAccountTransactionError
import org.example.project.domain.repositories.CurrentAccountDetailRepository
import org.example.project.domain.usecases.utils.GetCurrentDate

class AddTransaction(private val repository: CurrentAccountDetailRepository, private val getCurrentDate: GetCurrentDate) {

    suspend operator fun invoke(transaction: CurrentAccountTransaction):Long {

        when {
            transaction.type == TransactionType.NONE -> throw CurrentAccountTransactionError.NoTransactionType
            transaction.description.isBlank() -> throw CurrentAccountTransactionError.NoDescription
            transaction.amount == 0L -> throw CurrentAccountTransactionError.NoAmount
        }

        val today: LocalDate = getCurrentDate()

        return repository.addTransaction(transaction.copy(date = today))

    }

}