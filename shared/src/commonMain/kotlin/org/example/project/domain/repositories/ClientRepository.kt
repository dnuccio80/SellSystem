package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.Client

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    suspend fun addClient(client: Client)
}