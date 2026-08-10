package org.example.project.domain.usecases.expenses

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.example.project.domain.models.expense.ComposedExpenseFinance
import org.example.project.domain.repositories.ExpensesRepository
import kotlin.time.Clock

class GetComposedExpensesFinance(private val repository: ExpensesRepository,private val getSingleExpenseFinance: GetSingleExpenseFinance) {

    operator fun invoke(): Flow<ComposedExpenseFinance> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val todayExpenses = repository.getAllExpenses().map { expenses ->
            expenses.filter { expense ->
                expense.date?.let { date ->
                    date == today
                } == true
            }
        }

        val monthExpenses = repository.getAllExpenses().map { expenses ->
            expenses.filter { expense ->
                expense.date?.let { date ->
                    date.year == today.year &&
                            date.month == today.month
                } == true
            }
        }

        val previousMonthExpenses = repository.getAllExpenses().map { expenses ->
            expenses.filter { expense ->
                expense.date?.let { date ->
                    date.year == today.year &&
                            date == today.minus(1, DateTimeUnit.MONTH)
                } == true
            }
        }

        val threeMonthExpenses = repository.getAllExpenses().map { expenses ->
            expenses.filter { expense ->
                expense.date?.let { date ->
                    date >= today.minus(2, DateTimeUnit.MONTH)
                } == true
            }
        }

        val monthFinance = getSingleExpenseFinance(monthExpenses)
        val previousMonth = getSingleExpenseFinance(previousMonthExpenses)
        val threeMonthFinance = getSingleExpenseFinance(threeMonthExpenses)

        return combine(
            monthFinance,
            previousMonth,
            threeMonthFinance
        ) { month, previousMonth, threeMonths ->

            ComposedExpenseFinance(
                monthExpenseData = month,
                previousMonthExpenseData = previousMonth,
                threeMonthExpenseData = threeMonths
            )
        }

    }


}