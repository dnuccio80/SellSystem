package org.example.project.domain.usecases.clients

import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.domain.models.client.Client

class GetClients(private val clientRepository: ClientRepositoryImpl) {
    operator fun invoke(query: String): Flow<List<Client>> {

       return if(query.isBlank()) {
            clientRepository.getAllClients()
        } else {
            clientRepository.getClientBySearchQuery(query)
       }

    }
}