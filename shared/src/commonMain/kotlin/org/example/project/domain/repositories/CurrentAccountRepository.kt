package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.relations.ClientWithCurrentAccount
import org.example.project.domain.models.currentaccount.CurrentAccount

interface CurrentAccountRepository {
    fun getAllCurrentAccounts(): Flow<List<CurrentAccount>>
    fun getAllClientsWithCurrentAccount(): Flow<List<ClientWithCurrentAccount>>
    fun getClientsWithCurrentAccountByQuery(query:String):Flow<List<ClientWithCurrentAccount>>
    suspend fun getClientWithCurrentAccountById(id:Int): ClientWithCurrentAccount
    suspend fun addCurrentAccount(currentAccount: CurrentAccount)
}