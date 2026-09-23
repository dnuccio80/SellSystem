package org.example.project.domain.models.expense

import org.example.project.domain.repositories.ExpensesRepository
import org.example.project.domain.usecases.utils.GetCurrentDate

class AddExpense(private val repository: ExpensesRepository, private val getCurrentDate: GetCurrentDate) {

    suspend operator fun invoke(expense: Expense) {
        when {
            expense.description.isBlank() -> throw ExpenseError.EmptyDescription
            expense.amount == 0L -> throw ExpenseError.EmptyAmount
        }

        val date = getCurrentDate()

        repository.addExpense(expense.copy(description = expense.description.trim(), date = date))
    }

}