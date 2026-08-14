package org.example.project.domain.usecases.currentaccounts

import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction
import org.example.project.domain.models.currentaccount.CurrentAccountTransactionError
import org.example.project.domain.repositories.CurrentAccountDetailRepository

class AddTransaction(private val repository: CurrentAccountDetailRepository) {

    suspend operator fun invoke(transaction: CurrentAccountTransaction) {

        when {
            transaction.type == TransactionType.NONE -> throw CurrentAccountTransactionError.NoTransactionType
            transaction.description.isBlank() -> throw CurrentAccountTransactionError.NoDescription
            transaction.amount == 0L -> throw CurrentAccountTransactionError.NoAmount
        }

        repository.addTransaction(transaction)

    }



}