package org.example.project.ui.screens.currentaccounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.currentaccount.CurrentAccount
import org.example.project.domain.models.currentaccount.CurrentAccountFinancialReport
import org.example.project.domain.models.currentaccount.CurrentAccountSummary
import org.example.project.domain.models.sell.SellFinancialReport
import org.example.project.domain.repositories.ClientRepository
import org.example.project.domain.repositories.CurrentAccountRepository
import org.example.project.domain.usecases.clients.GetClients
import org.example.project.domain.usecases.currentaccounts.GetClientsWithNoCurrentAccounts
import org.example.project.domain.usecases.currentaccounts.GetCurrentAccountFinancialReport
import org.example.project.domain.usecases.currentaccounts.GetCurrentAccounts
import org.example.project.ui.screens.clients.CleanClient
import org.koin.core.qualifier._q
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class CurrentAccountsViewModel(
    private val currentAccountRepository: CurrentAccountRepository,
    private val clientRepository: ClientRepository,
    private val getCurrentAccountFinancialReport: GetCurrentAccountFinancialReport,
    clientsWithNoCurrentAccounts: GetClientsWithNoCurrentAccounts,
    getCurrentAccounts: GetCurrentAccounts,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _currentAccounts = _query.debounce(300.milliseconds).flatMapLatest { query ->
        getCurrentAccounts(query)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val currentAccounts = _currentAccounts

    private val _allCurrentAccounts = getCurrentAccounts("").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _clients = clientsWithNoCurrentAccounts().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    val clients = _clients

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _financialReport = _currentAccounts.mapLatest { list ->
        getCurrentAccountFinancialReport(list)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CurrentAccountFinancialReport(
        highestCurrentAccount = CurrentAccountSummary()
    ))
    val financialReport = _financialReport

    private val _selectedClient = MutableStateFlow(CleanClient().getCleanClient())
    val selectedClient = _selectedClient.asStateFlow()

    fun updateSelectedClient(id: Int) {
        viewModelScope.launch {
            val client = async { clientRepository.getClientById(id) }.await()
            _selectedClient.update { client }
        }
    }

    fun addCurrentAccount() {
        viewModelScope.launch {
            val account = CurrentAccount(
                clientId = _selectedClient.value.id,
                amount = 0L
            )
            currentAccountRepository.addCurrentAccount(account)
        }
    }

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }


}