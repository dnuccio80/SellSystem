package org.example.project.domain.usecases.currentaccounts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction
import org.example.project.domain.repositories.CurrentAccountDetailRepository

class GetPaymentsByClient(private val repository: CurrentAccountDetailRepository) {
    operator fun invoke(clientId:Int): Flow<List<CurrentAccountTransaction>> {
        return repository.getAllTransactions(clientId).map { list ->
            list.filter { it.type == TransactionType.PAYMENT }
        }
    }
}