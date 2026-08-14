package org.example.project.ui.screens.currentaccounts

import org.example.project.domain.models.currentaccount.ClientWithCurrentAccount
import org.example.project.domain.models.currentaccount.CurrentAccount

class CleanCurrentAccount {
    fun getCleanCurrentAccount(): CurrentAccount {
        return CurrentAccount(
            id = 0,
            clientId = 0,
            amount = 0L
        )
    }
}