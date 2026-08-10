package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.expense.Expense
import org.example.project.domain.repositories.ExpensesRepository

class ExpenseRepositoryImpl(private val db: SystemDatabase): ExpensesRepository {
    override fun getAllExpenses(): Flow<List<Expense>> {
        return db.expenseDao().getAllExpenses().map { list ->
            list.map { it.toDomain() }.reversed()
        }
    }

    override fun getExpensesByQuery(query: String): Flow<List<Expense>> {
        return db.expenseDao().getExpensesByQuery(query).map { list ->
            list.map { it.toDomain() }.reversed()
        }
    }

    override suspend fun getExpenseById(id: Int): Expense {
        return db.expenseDao().getExpenseById(id).toDomain()
    }

    override suspend fun addExpense(expense: Expense) {
        db.expenseDao().addExpense(expense.toEntity())
    }

    override suspend fun deleteExpenseById(id: Int) {
        db.expenseDao().deleteExpenseById(id)
    }
}