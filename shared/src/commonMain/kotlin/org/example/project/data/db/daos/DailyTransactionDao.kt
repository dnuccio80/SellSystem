package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.currentaccount.DailyTransactionEntity

@Dao
interface DailyTransactionDao {

    @Query("SELECT * FROM DailyTransactionEntity")
    fun getAllDailyTransactions(): Flow<List<DailyTransactionEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addDailyTransaction(transaction: DailyTransactionEntity)

    @Query("DELETE FROM DailyTransactionEntity WHERE id = :id")
    suspend fun deleteDailyTransactionById(id:Int)

}