package org.example.project.domain.models.expense

import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.example.project.domain.repositories.ExpensesRepository
import kotlin.time.Clock

class AddExpense(private val repository: ExpensesRepository) {

    suspend operator fun invoke(expense: Expense) {
        when {
            expense.description.isBlank() -> throw ExpenseError.EmptyDescription
            expense.amount == 0L -> throw ExpenseError.EmptyAmount
        }

        val date = Clock.System.todayIn(TimeZone.currentSystemDefault())

        repository.addExpense(expense.copy(description = expense.description.trim(), date = date))
    }

}