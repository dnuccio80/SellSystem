package org.example.project.domain.models.currentaccount

import org.example.project.domain.models.client.Client

data class ClientWithCurrentAccount(
    val client: Client,
    val currentAccount: CurrentAccount?
)