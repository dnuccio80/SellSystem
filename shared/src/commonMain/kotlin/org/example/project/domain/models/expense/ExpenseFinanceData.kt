package org.example.project.domain.models.expense

import kotlinx.datetime.Month

data class ExpenseFinanceData(
    val expensesQuantity: Int = 0,
    val totalAmount: Long = 0L,
    val maxExpense: Expense? = null
)

data class ComposedExpenseFinance(
    val monthExpenseData: ExpenseFinanceData = ExpenseFinanceData(),
    val previousMonthExpenseData: ExpenseFinanceData = ExpenseFinanceData(),
    val threeMonthExpenseData: ExpenseFinanceData = ExpenseFinanceData()
)
