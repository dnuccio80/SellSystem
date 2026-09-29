package org.example.project.ui.screens.currentaccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.CleanCurrentAccountTransaction
import org.example.project.domain.models.currentaccount.CurrentAccountBalance
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction
import org.example.project.domain.models.currentaccount.CurrentAccountTransactionError
import org.example.project.domain.repositories.ClientRepository
import org.example.project.domain.repositories.CurrentAccountDetailRepository
import org.example.project.domain.usecases.currentaccounts.AddTransaction
import org.example.project.domain.usecases.currentaccounts.CurrentAccountDetailsUiState
import org.example.project.domain.usecases.currentaccounts.GetCurrentAccountBalance
import org.example.project.domain.usecases.currentaccounts.GetPaymentsByClient
import org.example.project.domain.usecases.currentaccounts.GetPurchasesByClient
import org.example.project.ui.screens.clients.CleanClient
import kotlin.collections.emptyList

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentAccountDetailsViewModel(
    private val transactionsRepo: CurrentAccountDetailRepository,
    private val clientRepo: ClientRepository,
    private val addTransaction: AddTransaction,
    getPurchasesByClient: GetPurchasesByClient,
    getPaymentsByClient: GetPaymentsByClient,
    getCurrentAccountBalance: GetCurrentAccountBalance,
) : ViewModel() {


    private val _clientId = MutableStateFlow(0)
    private val _newTransactionData =
        MutableStateFlow(CleanCurrentAccountTransaction().getCleanCurrentAccountTransaction())

    private val _client = MutableStateFlow(CleanClient().getCleanClient())
    private val _purchases = _clientId.filter { it != 0 }.flatMapLatest { id ->
        getPurchasesByClient(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _payments = _clientId.filter { it != 0 }.flatMapLatest { id ->
        getPaymentsByClient(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _balance = _clientId.filter { it != 0 }.flatMapLatest { id ->
        getCurrentAccountBalance(id)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CurrentAccountBalance(0L, 0L, 0L)
    )
    private val _uiState = combine(
        _client,
        _newTransactionData,
        _purchases,
        _payments,
        _balance,
    ) { client, transactionData, purchases, payments, balance ->
        CurrentAccountDetailsUiState.Success(
            client = client,
            balance = balance,
            transactionData = transactionData,
            purchasesList = purchases,
            paymentsList = payments
        ) as CurrentAccountDetailsUiState
    }.catch { e -> CurrentAccountDetailsUiState.Error(e) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CurrentAccountDetailsUiState.Loading
        )
    val uiState = _uiState

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun loadCurrentAccount(id: Int) {
        viewModelScope.launch {
            _clientId.update { id }
            _client.update { clientRepo.getClientById(id) }
        }
    }

    fun updateDescriptionTransactionData(newValue: String) {
        _newTransactionData.update { it.copy(description = newValue) }
    }

    fun updateAmountTransactionData(newValue: String) {
        val value = if (newValue.isBlank()) 0L else newValue.toLong()
        _newTransactionData.update { it.copy(amount = value) }
    }

    fun updateTransactionType(newValue: TransactionType) {
        _newTransactionData.update { it.copy(type = newValue) }
    }

    fun updateTransaction(transaction: CurrentAccountTransaction) {
        _newTransactionData.update { transaction }
    }

    fun tryAddTransaction(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                addTransaction(_newTransactionData.value)
                cleanTransactionData()
                onDone()
            } catch (e: CurrentAccountTransactionError) {
                _events.emit(e.msg)
            }
        }
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            try {
                transactionsRepo.deleteTransaction(_newTransactionData.value)
                cleanTransactionData()
            } catch (e: CurrentAccountTransactionError) {
                _events.emit(e.msg)
            }
        }
    }

    fun cleanTransactionData() {
        _newTransactionData.update {
            CleanCurrentAccountTransaction().getCleanCurrentAccountTransaction()
                .copy(clientId = _clientId.value)
        }
    }


}