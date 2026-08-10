package org.example.project.domain.usecases.expenses

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.example.project.domain.models.expense.Expense
import org.example.project.domain.models.expense.ExpenseFinanceData

class GetSingleExpenseFinance {

    operator fun invoke(
        expenses: Flow<List<Expense>>
    ): Flow<ExpenseFinanceData> {


        return expenses.map { list ->

            val maxExpense = list.maxByOrNull { it.amount }

            ExpenseFinanceData(
                expensesQuantity = list.size,
                totalAmount = list.sumOf { it.amount },
                maxExpense = maxExpense
            )
        }
    }
}