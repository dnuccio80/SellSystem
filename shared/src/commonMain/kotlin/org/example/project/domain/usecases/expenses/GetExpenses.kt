package org.example.project.domain.usecases.expenses

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.example.project.domain.models.expense.Expense
import org.example.project.domain.repositories.ExpensesRepository
import org.example.project.ui.screens.expenses.ExpenseFilterLabel
import kotlin.collections.filter
import kotlin.time.Clock

class GetExpenses(private val repository: ExpensesRepository) {
    operator fun invoke(query: String, labelSelected: String): Flow<List<Expense>> {
        return if (query.isNotBlank()) {
            getFilteredList(repository.getExpensesByQuery(query), labelSelected)
        } else {
            getFilteredList(repository.getAllExpenses(), labelSelected)
        }
    }
}
private fun getFilteredList(list: Flow<List<Expense>>, filter: String): Flow<List<Expense>> {

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    return when (filter) {
        ExpenseFilterLabel.TODAY.etiquette -> {
            list.map { list ->
                list.filter { it.date?.year == today.year && it.date.dayOfYear == today.dayOfYear }
            }
        }

        ExpenseFilterLabel.WEEK.etiquette -> {
            list.map { list ->
                list.filter {
                    it.date?.year == today.year && it.date >= today.minus(
                        7,
                        DateTimeUnit.DAY
                    )
                }
            }
        }

        ExpenseFilterLabel.MONTH.etiquette -> {
            list.map { list ->
                list.filter {
                    it.date?.year == today.year && it.date.month == today.month
                }
            }
        }

        ExpenseFilterLabel.THREE_MONTHS.etiquette -> {
            list.map { list ->
                list.filter {
                    it.date?.year == today.year && it.date >= today.minus(
                        3,
                        DateTimeUnit.MONTH
                    )
                }
            }
        }
        else -> {
            list
        }
    }
}