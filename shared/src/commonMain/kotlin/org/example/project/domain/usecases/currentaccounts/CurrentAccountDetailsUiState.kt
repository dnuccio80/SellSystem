package org.example.project.domain.usecases.currentaccounts

import org.example.project.domain.models.client.Client
import org.example.project.domain.models.currentaccount.CurrentAccountBalance
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction

sealed class CurrentAccountDetailsUiState {
    data object Loading: CurrentAccountDetailsUiState()
    data class Error(val throwable: Throwable): CurrentAccountDetailsUiState()
    data class Success(
        val client: Client,
        val balance: CurrentAccountBalance,
        val transactionData:CurrentAccountTransaction,
        val purchasesList:List<CurrentAccountTransaction>,
        val paymentsList:List<CurrentAccountTransaction>,
    ) : CurrentAccountDetailsUiState()
}
