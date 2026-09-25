package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.CurrentAccountTransactionEntity

@Dao
interface CurrentAccountTransactionDao {

    @Query("SELECT * FROM CurrentAccountTransactionEntity WHERE clientId = :clientId ORDER BY date, id DESC")
    fun getTransactions(clientId:Int): Flow<List<CurrentAccountTransactionEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addTransaction(transaction: CurrentAccountTransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: CurrentAccountTransactionEntity)

}