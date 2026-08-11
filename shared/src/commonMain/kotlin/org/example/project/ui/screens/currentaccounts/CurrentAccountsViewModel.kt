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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.currentaccount.CurrentAccount
import org.example.project.domain.repositories.ClientRepository
import org.example.project.domain.repositories.CurrentAccountRepository
import org.example.project.domain.usecases.clients.GetClients
import org.example.project.domain.usecases.currentaccounts.GetClientsWithNoCurrentAccounts
import org.example.project.domain.usecases.currentaccounts.GetCurrentAccounts
import org.example.project.ui.screens.clients.CleanClient
import org.koin.core.qualifier._q
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class CurrentAccountsViewModel(
    private val currentAccountRepository: CurrentAccountRepository,
    private val clientRepository: ClientRepository,
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

    private val _clients = clientsWithNoCurrentAccounts().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    val clients = _clients

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
            val random = (1..15000).random()
            val account = CurrentAccount(
                clientId = _selectedClient.value.id,
                amount = random.toLong()
            )
            currentAccountRepository.addCurrentAccount(account)
        }
    }

    fun updateQuery(newValue: String) {
        _query.update { newValue }
    }


}