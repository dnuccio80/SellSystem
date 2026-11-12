package org.example.project.ui.screens.loyalty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.project.domain.repositories.LoyaltyRepository

class LoyaltyViewModel(repository: LoyaltyRepository) : ViewModel() {


    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _loyaltyAccounts = repository.getAllLoyaltyAccounts().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val loyaltyAccounts = _loyaltyAccounts

    fun updateQuery(newValue:String) {
        _query.update { newValue }
    }

}