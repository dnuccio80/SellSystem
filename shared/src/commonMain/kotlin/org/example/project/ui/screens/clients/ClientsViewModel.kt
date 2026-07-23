package org.example.project.ui.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.newStringBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.models.Client
import org.example.project.domain.usecases.clients.AddNewClient
import org.example.project.domain.usecases.clients.GetClients

class ClientsViewModel(
    getClients: GetClients,
    private val addNewClient: AddNewClient,
) : ViewModel() {

    private val _clientList =
        getClients().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
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

    fun cleanData() {
        _clientData.update { current ->
            current.copy(fullName = "", phoneNumber = 0, address = "", birthday = "", notes = "")
        }
    }

    fun addClient(onDone:() -> Unit) {
        viewModelScope.launch {
            if (isAllDataCorrect()) {
                addNewClient(_clientData.value)
                _events.emit("Cliente agregado con éxito!")
                onDone()
            } else {
                _events.emit("Faltan rellenar datos!")
            }
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