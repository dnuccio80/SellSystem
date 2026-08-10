package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ExpenseEntity

@Dao
interface ExpensesDao {
    @Query("SELECT * FROM ExpenseEntity")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM ExpenseEntity WHERE description LIKE '%' || :query || '%' ")
    fun getExpensesByQuery(query:String):Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM ExpenseEntity WHERE id = :id")
    suspend fun getExpenseById(id:Int): ExpenseEntity

    @Insert(onConflict = REPLACE)
    suspend fun addExpense(expense: ExpenseEntity)

    @Query("DELETE FROM ExpenseEntity WHERE id = :id")
    suspend fun deleteExpenseById(id:Int)
}
