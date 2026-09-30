package org.example.project.ui.screens.pendingorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.pendingorders.CleanPendingOrder
import org.example.project.domain.models.pendingorders.PendingOrderError
import org.example.project.domain.models.pendingorders.PendingOrderPriority
import org.example.project.domain.models.pendingorders.PendingOrderState
import org.example.project.domain.repositories.PendingOrdersRepository
import org.example.project.domain.usecases.pendingorders.AddPendingOrder
import org.example.project.domain.usecases.pendingorders.GetPendingOrders
import org.example.project.domain.usecases.pendingorders.PendingOrderTarget
import org.example.project.ui.screens.pendingorders.UpdatePendingOrderAction.*
import kotlin.time.Duration.Companion.milliseconds

enum class UpdatePendingOrderAction {
    CLIENT, PHONE, ADDRESS, DESCRIPTION, STATUS, PRIORITY
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class PendingOrdersViewModel(
    private val getPendingOrders: GetPendingOrders,
    private val addPendingOrder: AddPendingOrder,
    private val pendingOrdersRepository: PendingOrdersRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _watchFilter = listOf(
        PendingOrderTarget.ACTIVE,
        PendingOrderTarget.DONE
    )

    private val _watchFilterSelected = MutableStateFlow(_watchFilter.first())
    val watchFilterSelected = _watchFilterSelected.asStateFlow()

    private val _pendingOrders = combine(_query, _watchFilterSelected) { query, filter ->
        query to filter
    }.debounce(300.milliseconds).flatMapLatest { (query, filter) ->
        getPendingOrders(query, filter)
    }.catch { e ->

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val pendingOrders = _pendingOrders

    private val _newPendingOrderData = MutableStateFlow(CleanPendingOrder().getNew())
    val newPendingOrderData = _newPendingOrderData.asStateFlow()

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun updatePendingOrderData(value: String, action: UpdatePendingOrderAction) {

        when (action) {
            CLIENT -> {
                _newPendingOrderData.update { it.copy(clientName = value) }
            }

            PHONE -> {
                val inLong = if (value.isNotBlank()) value.toLong() else 0L
                _newPendingOrderData.update { it.copy(phone = inLong) }
            }

            ADDRESS -> {
                _newPendingOrderData.update { it.copy(address = value) }
            }

            DESCRIPTION -> {
                _newPendingOrderData.update { it.copy(description = value) }
            }

            STATUS -> {
                val status = PendingOrderState.entries.first { it.etiquette == value }
                _newPendingOrderData.update { it.copy(status = status) }
            }

            PRIORITY -> {
                val priority = PendingOrderPriority.entries.first { it.etiquette == value }
                _newPendingOrderData.update { it.copy(priority = priority) }
            }
        }

    }

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }

    fun getPendingOrderAndUpdatePendingOrderDataById(id: Int) {
        val pendingOrder = _pendingOrders.value.first { it.id == id }
        _newPendingOrderData.update { pendingOrder }
    }

    fun tryAddPendingOrder(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                addPendingOrder(_newPendingOrderData.value)
                cleanPendingOrderData()
                onDone()
            } catch (e: PendingOrderError) {
                _events.emit(e.msg)
            }
        }
    }

    fun deletePendingOrder() {
        viewModelScope.launch {
            pendingOrdersRepository.deletePendingOrder(_newPendingOrderData.value.id)
            cleanPendingOrderData()
        }
    }

    fun cleanPendingOrderData() {
        _newPendingOrderData.update { CleanPendingOrder().getNew() }
    }

    fun updatePendingOrderTarget(target: PendingOrderTarget) {
        _watchFilterSelected.update { target }
    }


}