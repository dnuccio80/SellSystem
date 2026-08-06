package org.example.project.ui.screens.clients

import org.example.project.domain.models.client.Client

class CleanClient {

    fun getCleanClient(): Client {
        return Client(
            fullName = "",
            phoneNumber = 0L,
            address = "",
            birthday = null,
            notes = "",
            loyaltyPoints = 0,
            hasCurrentAccount = false,
            city = "",
            province = ""
        )
    }

}