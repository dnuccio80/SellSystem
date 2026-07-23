package org.example.project.ui.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.newStringBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.Client
import org.example.project.domain.usecases.clients.AddNewClient
import org.example.project.domain.usecases.clients.GetClientById
import org.example.project.domain.usecases.clients.GetClients
import kotlin.time.Duration.Companion.milliseconds

class ClientsViewModel(
    getClients: GetClients,
    private val addNewClient: AddNewClient,
    private val getClientById: GetClientById
) : ViewModel() {

    private val _queryClientName = MutableStateFlow("")
    val queryClientName = _queryClientName.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _clientList = queryClientName.debounce(300.milliseconds).flatMapLatest { query ->
        getClients(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val clientList = _clientList

    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    private val _clientData = MutableStateFlow(
        Client(
            fullName = "",
            phoneNumber = 0,
            address = "",
            birthday = "",
            notes = ""
        )
    )
    val clientData = _clientData.asStateFlow()

    fun updateName(newValue: String) {
        _clientData.update { current ->
            current.copy(fullName = newValue)
        }
    }

    fun updatePhoneNumber(newValue: String) {
        val phone = if (newValue.isBlank()) 0L else newValue.toLong()
        _clientData.update { current ->
            current.copy(phoneNumber = phone)
        }
    }

    fun updateAddress(newValue: String) {
        _clientData.update { current ->
            current.copy(address = newValue)
        }
    }

    fun updateBirthDay(newValue: String) {
        _clientData.update { current ->
            current.copy(birthday = newValue)
        }
    }

    fun updateNotes(newValue: String) {
        _clientData.update { current ->
            current.copy(notes = newValue)
        }
    }

    fun updateQuerySearch(newValue:String) {
        _queryClientName.value = newValue
    }
    fun addClient(onDone:() -> Unit) {
        viewModelScope.launch {
            if (isAllDataCorrect()) {
                addNewClient(_clientData.value)
                onDone()
            } else {
                _events.emit("Faltan rellenar datos!")
            }
        }
    }

    fun getClientData(id:Int, onDone: () -> Unit) {
        viewModelScope.launch {
            val client = async {
                getClientById(id)
            }.await()

            _clientData.update { current ->
                current.copy(
                    id = client.id,
                    fullName = client.fullName,
                    phoneNumber = client.phoneNumber,
                    address = client.address,
                    birthday = client.birthday,
                    notes = client.notes,
                    loyaltyPoints = client.loyaltyPoints,
                    hasCurrentAccount = client.hasCurrentAccount
                )
            }
            onDone()
        }
    }

    fun cleanData() {
        _clientData.update { current ->
            current.copy(id = 0, fullName = "", phoneNumber = 0, address = "", birthday = "", notes = "", loyaltyPoints = 0, hasCurrentAccount = false)
        }
    }
    fun isAllDataCorrect(): Boolean {
        return _clientData.value.fullName.isNotBlank() &&
                _clientData.value.phoneNumber != 0L &&
                _clientData.value.address.isNotBlank() &&
                _clientData.value.birthday.isNotBlank() &&
                _clientData.value.fullName.isNotBlank()
    }


}