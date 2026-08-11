package org.example.project.domain.usecases.clients

import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.domain.models.client.Client
import org.example.project.domain.repositories.ClientRepository

class GetClientById(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(clientId:Int): Client = clientRepository.getClientById(clientId)
}