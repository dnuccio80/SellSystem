package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.daily.DailyTransaction

interface DailyTransactionRepository {
    fun getAllDailyTransactions(): Flow<List<DailyTransaction>>
    suspend fun addDailyTransaction(transaction: DailyTransaction)
    suspend fun deleteDailyTransactionById(id:Int)
}