package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.CurrentAccountTransactionEntity
import org.example.project.domain.models.currentaccount.ClientWithTransactions
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction

interface CurrentAccountDetailRepository {
    fun getAllTransactions(clientId:Int): Flow<List<CurrentAccountTransaction>>
    suspend fun addTransaction(transaction: CurrentAccountTransaction)
    suspend fun deleteTransaction(transaction: CurrentAccountTransaction)
}