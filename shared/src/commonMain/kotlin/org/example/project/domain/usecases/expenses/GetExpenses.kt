package org.example.project.domain.usecases.expenses

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.expense.Expense
import org.example.project.domain.repositories.ExpensesRepository

class GetExpenses(private val repository: ExpensesRepository) {
    operator fun invoke(query: String): Flow<List<Expense>> {
        return if(query.isNotBlank()) {
            repository.getExpensesByQuery(query)
        }else{
            repository.getAllExpenses()
        }
    }
}