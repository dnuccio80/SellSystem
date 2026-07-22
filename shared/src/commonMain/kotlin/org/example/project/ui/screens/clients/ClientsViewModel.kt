package org.example.project.ui.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.example.project.domain.usecases.clients.GetClients

class ClientsViewModel(
    getClients: GetClients
): ViewModel() {

    private val _clientList = getClients().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val clientList = _clientList

}