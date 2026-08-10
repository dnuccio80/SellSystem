package org.example.project.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.expense.AddExpense
import org.example.project.domain.models.expense.ComposedExpenseFinance
import org.example.project.domain.models.expense.Expense
import org.example.project.domain.models.expense.ExpenseError
import org.example.project.domain.repositories.ExpensesRepository
import org.example.project.domain.usecases.expenses.GetComposedExpensesFinance
import org.example.project.domain.usecases.expenses.GetExpenses
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.milliseconds

class ExpensesViewModel(
    getExpenses: GetExpenses,
    private val expensesFinance: GetComposedExpensesFinance,
    private val repository: ExpensesRepository,
    private val addExpense: AddExpense,

    ): ViewModel() {

    private val _expenseData = MutableStateFlow(Expense(description = "", amount = 0L))
    val expenseData = _expenseData.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _expenses = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getExpenses(query)
    }.catch { e ->

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val expenses = _expenses

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _composedFinance = expensesFinance().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        ComposedExpenseFinance()
    )
    val composedFinance = _composedFinance

    fun addExpense(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                addExpense(_expenseData.value)
                cleanExpenseData()
                onDone()
            }catch (e: ExpenseError) {
                _events.emit(e.msg)
            }
        }
    }

    fun updateDescription(newValue:String) {
        _expenseData.update { current -> current.copy(description = newValue) }
    }

    fun updateAmount(newValue:String) {
        val longValue = if (newValue.isBlank()) 0L else newValue.toLong()
        _expenseData.update { current -> current.copy(amount = longValue) }
    }

    fun updateQuery(newValue:String) {
        _query.update { newValue }
    }

    fun getExpenseDataFromId(id:Int, onDone:() -> Unit) {
        viewModelScope.launch {
            val data = async { repository.getExpenseById(id) }.await()
            _expenseData.update { data }
            onDone()
        }
    }

    fun deleteExpense() {
        viewModelScope.launch {
            repository.deleteExpenseById(_expenseData.value.id)
            cleanExpenseData()
        }
    }

    fun cleanExpenseData() {
        _expenseData.update { Expense(id = 0, description = "", amount = 0L) }
    }


}