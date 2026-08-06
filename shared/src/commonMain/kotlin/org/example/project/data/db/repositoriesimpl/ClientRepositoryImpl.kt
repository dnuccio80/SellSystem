package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.client.Client
import org.example.project.domain.repositories.ClientRepository

class ClientRepositoryImpl(private val db: SystemDatabase): ClientRepository {
    override fun getAllClients(): Flow<List<Client>> {
        return db.clientDao().getAllClients().map { list ->
            list.map { client ->
                client.toDomain()
            }
        }
    }

    override fun getClientBySearchQuery(query: String): Flow<List<Client>> {
        return db.clientDao().getClientsByQuery(query).map { list ->
            list.map { client ->
                client.toDomain()
            }
        }
    }

    override suspend fun getClientById(id: Int): Client {
        return db.clientDao().getClientById(id).toDomain()
    }

    override suspend fun addClient(client: Client) {
        db.clientDao().addClient(client.toEntity())
    }
}