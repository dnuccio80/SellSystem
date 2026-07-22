package org.example.project.domain.usecases.clients

import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.domain.models.Client

class GetClients(private val clientRepository: ClientRepositoryImpl) {
    operator fun invoke(): Flow<List<Client>> = clientRepository.getAllClients()
}