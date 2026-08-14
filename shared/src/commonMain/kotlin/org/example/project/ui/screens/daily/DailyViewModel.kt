package org.example.project.ui.screens.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.ui.screens.daily.DailyDataAction.UPDATE_CASH
import org.example.project.ui.screens.daily.DailyDataAction.UPDATE_VIRTUAL_ACCOUNT

enum class DailyDataAction {
    UPDATE_CASH, UPDATE_VIRTUAL_ACCOUNT
}

class DailyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DailyUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun openFinance(onDone:() -> Unit) {
        if(isInitialDataCorrect()) {
            _uiState.update { it.copy(isOpen = true) }
            onDone()

        } else {
            viewModelScope.launch {
                _events.emit("Faltan rellenar datos")
            }
        }
    }

    fun closeFinance() {
        _uiState.update { it.copy(isOpen = false) }
    }

    fun updateDailyData(action: DailyDataAction, value: String) {

        val valueInLong = value.toLongOrNull() ?: 0L

        when (action) {
            UPDATE_CASH -> {
                _uiState.update { it.copy(dailyData = it.dailyData.copy(initialCashAmount = valueInLong)) }
            }

            UPDATE_VIRTUAL_ACCOUNT -> {
                _uiState.update { it.copy(dailyData = it.dailyData.copy(initialVirtualAccountAmount = valueInLong)) }
            }
        }
    }

    fun cleanData() {
        _uiState.update { it.copy(dailyData = it.dailyData.copy(initialCashAmount = 0L, initialVirtualAccountAmount = 0L)) }
    }
    private fun isInitialDataCorrect(): Boolean {
        return _uiState.value.dailyData.initialCashAmount != 0L && _uiState.value.dailyData.initialVirtualAccountAmount != 0L
    }

}