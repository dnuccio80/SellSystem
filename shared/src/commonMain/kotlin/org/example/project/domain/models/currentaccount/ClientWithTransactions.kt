package org.example.project.domain.models.currentaccount

import org.example.project.domain.models.client.Client

data class ClientWithTransactions(
    val client: Client,
    val transactionList: List<CurrentAccountTransaction>
)
