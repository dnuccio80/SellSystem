package org.example.project.domain.usecases.currentaccounts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.CurrentAccountBalance
import org.example.project.domain.repositories.CurrentAccountDetailRepository

class GetCurrentAccountBalance(
    private val repository: CurrentAccountDetailRepository,
) {

    operator fun invoke(clientId:Int): Flow<CurrentAccountBalance> {

        return repository.getAllTransactions(clientId).map { list ->

            val purchasesBalance = list.filter { it.type == TransactionType.PURCHASE }.sumOf { it.amount }
            val paymentsBalance = list.filter { it.type == TransactionType.PAYMENT }.sumOf { it.amount }

            val balance = purchasesBalance - paymentsBalance

            CurrentAccountBalance(purchasesAmount = purchasesBalance, paymentsAmount = paymentsBalance, balance)
        }


    }

}