package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.Client

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    fun getClientBySearchQuery(query:String):Flow<List<Client>>
    suspend fun getClientById(id:Int): Client
    suspend fun addClient(client: Client)
}