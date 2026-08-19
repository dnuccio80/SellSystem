package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.daily.DailyTransaction
import org.example.project.domain.repositories.DailyTransactionRepository

class DailyTransactionRepositoryImpl(private val db: SystemDatabase): DailyTransactionRepository {
    override fun getAllDailyTransactions(): Flow<List<DailyTransaction>> {
        return db.dailyTransactionDao().getAllDailyTransactions().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addDailyTransaction(transaction: DailyTransaction) {
        db.dailyTransactionDao().addDailyTransaction(transaction.toEntity())
    }

    override suspend fun deleteDailyTransactionById(id: Int) {
        db.dailyTransactionDao().deleteDailyTransactionById(id)
    }
}