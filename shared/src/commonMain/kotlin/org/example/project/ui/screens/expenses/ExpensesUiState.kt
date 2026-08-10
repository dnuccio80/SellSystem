package org.example.project.ui.screens.expenses

import org.example.project.domain.models.expense.ComposedExpenseFinance
import org.example.project.domain.models.expense.Expense

sealed class ExpensesUiState() {
    data object Loading: ExpensesUiState()
    data class Error(val throwable: Throwable): ExpensesUiState()
    data class Success(
        val labelSelected:String,
        val expenseData: Expense,
        val expenses:List<Expense>,
        val composedFinance:ComposedExpenseFinance,
        val query:String
    ): ExpensesUiState()
}
