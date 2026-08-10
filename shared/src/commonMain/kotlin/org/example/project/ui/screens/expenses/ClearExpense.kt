package org.example.project.ui.screens.expenses

import org.example.project.domain.models.expense.Expense

class ClearExpense {
    fun getClearExpense(): Expense {
        return Expense(
            id = 0,
            description = "",
            amount = 0L,
            date = null
        )
    }
}