package org.example.project.domain.usecases.clients

import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.domain.models.Client

class AddNewClient(private val clientRepository: ClientRepositoryImpl) {
    suspend operator fun invoke(client: Client) {
        val name = client.fullName.trim()
        val address = client.address.trim()

        clientRepository.addClient(client.copy(fullName = name, address = address))
    }
}