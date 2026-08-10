package org.example.project.domain.repositories

import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ExpenseEntity
import org.example.project.domain.models.expense.Expense

interface ExpensesRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesByQuery(query:String):Flow<List<Expense>>
    suspend fun getExpenseById(id:Int): Expense
    suspend fun addExpense(expense: Expense)
    suspend fun deleteExpenseById(id:Int)
}