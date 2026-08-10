package org.example.project.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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
import org.example.project.ui.screens.expenses.ExpenseFilterLabel.*
import kotlin.collections.emptyList
import kotlin.math.exp
import kotlin.time.Duration.Companion.milliseconds

enum class ExpenseFilterLabel(val etiquette: String) {
    TODAY("Hoy"), WEEK("Esta semana"), MONTH("Este mes"), THREE_MONTHS("Últimos 3 meses"), ALL("Todo")
}

class ExpensesViewModel(
    getExpenses: GetExpenses,
    private val expensesFinance: GetComposedExpensesFinance,
    private val repository: ExpensesRepository,
    private val addExpense: AddExpense,

    ) : ViewModel() {

    val labelFilterList = listOf(
        ALL.etiquette,
        TODAY.etiquette,
        WEEK.etiquette,
        MONTH.etiquette,
        THREE_MONTHS.etiquette,
    )
    private val _labelSelected = MutableStateFlow(labelFilterList.first())

    private val _expenseData = MutableStateFlow(ClearExpense().getClearExpense())

    private val _query = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _expenses: StateFlow<List<Expense>> = combine(_query, _labelSelected)
    {query, label ->
        query to label
    }.debounce(300.milliseconds).flatMapLatest { (query, filter) ->
        getExpenses(query, filter)
    }.catch { e ->  }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _composedFinance = expensesFinance().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        ComposedExpenseFinance()
    )

    private val _uiState = combine(
        _labelSelected,
        _expenseData,
        _expenses,
        _composedFinance,
        _query
    ) { label, expenseData, expensesList, composedFinance, query ->
        ExpensesUiState.Success(
            labelSelected = label,
            expenseData = expenseData,
            expenses = expensesList,
            composedFinance = composedFinance,
            query = query
        ) as ExpensesUiState
    }.catch { e ->
        ExpensesUiState.Error(e)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpensesUiState.Loading)
    val uiState = _uiState

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun addExpense(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                addExpense(_expenseData.value)
                cleanExpenseData()
                onDone()
            } catch (e: ExpenseError) {
                _events.emit(e.msg)
            }
        }
    }

    fun updateDescription(newValue: String) {
        _expenseData.update { current -> current.copy(description = newValue) }
    }

    fun updateAmount(newValue: String) {
        val longValue = if (newValue.isBlank()) 0L else newValue.toLong()
        _expenseData.update { current -> current.copy(amount = longValue) }
    }

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }

    fun updateLabel(newValue: String) {
        _labelSelected.update { newValue }
    }

    fun getExpenseDataFromId(id: Int, onDone: () -> Unit) {
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
        _expenseData.update { ClearExpense().getClearExpense() }
    }


}