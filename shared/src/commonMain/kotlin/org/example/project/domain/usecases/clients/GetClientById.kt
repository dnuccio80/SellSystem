package org.example.project.domain.usecases.clients

import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.domain.models.Client

class GetClientById(private val clientRepository: ClientRepositoryImpl) {
    suspend operator fun invoke(clientId:Int): Client = clientRepository.getClientById(clientId)
}