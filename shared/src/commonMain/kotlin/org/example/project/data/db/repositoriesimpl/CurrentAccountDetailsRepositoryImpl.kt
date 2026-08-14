package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction
import org.example.project.domain.repositories.CurrentAccountDetailRepository

class CurrentAccountDetailsRepositoryImpl(private val db: SystemDatabase): CurrentAccountDetailRepository {
    override fun getAllTransactions(clientId:Int): Flow<List<CurrentAccountTransaction>> {
        return db.currentAccountTransactionDao().getTransactions(clientId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addTransaction(transaction: CurrentAccountTransaction) {
        db.currentAccountTransactionDao().addTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: CurrentAccountTransaction) {
        db.currentAccountTransactionDao().deleteTransaction(transaction.toEntity())
    }
}