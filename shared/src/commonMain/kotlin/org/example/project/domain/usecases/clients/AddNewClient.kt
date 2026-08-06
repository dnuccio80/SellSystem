package org.example.project.domain.usecases.clients

import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.domain.models.client.Client
import org.example.project.domain.models.client.ClientError

class AddNewClient(private val clientRepository: ClientRepositoryImpl) {
    suspend operator fun invoke(client: Client) {

        when {
            client.fullName.isBlank() -> throw ClientError.EmptyName
            client.phoneNumber == 0L -> throw ClientError.EmptyPhone
            client.address.isBlank() -> throw ClientError.NoAddress
            client.city.isBlank() -> throw ClientError.NoCity
            client.province.isBlank() -> throw ClientError.NoProvince
        }

        val name = client.fullName.trim()
        val address = client.address.trim()

        clientRepository.addClient(client.copy(fullName = name, address = address))
    }
}