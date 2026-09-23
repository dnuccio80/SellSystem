package org.example.project.domain.usecases.expenses

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.example.project.domain.models.expense.Expense
import org.example.project.domain.repositories.ExpensesRepository
import org.example.project.domain.usecases.utils.GetCurrentDate
import org.example.project.ui.screens.expenses.ExpenseFilterLabel
import kotlin.collections.filter

class GetExpenses(private val repository: ExpensesRepository, private val getCurrentDate: GetCurrentDate) {
    operator fun invoke(query: String, labelSelected: String): Flow<List<Expense>> {
        return if (query.isNotBlank()) {
            getFilteredList(repository.getExpensesByQuery(query), labelSelected,getCurrentDate())
        } else {
            getFilteredList(repository.getAllExpenses(), labelSelected,getCurrentDate())
        }
    }
}
private fun getFilteredList(list: Flow<List<Expense>>, filter: String, today: LocalDate): Flow<List<Expense>> {

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