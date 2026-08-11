package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.data.db.entities.relations.ClientWithCurrentAccount
import org.example.project.domain.models.currentaccount.CurrentAccount
import org.example.project.domain.repositories.CurrentAccountRepository

class CurrentAccountRepositoryImpl(private val db: SystemDatabase): CurrentAccountRepository {
    override fun getAllCurrentAccounts(): Flow<List<CurrentAccount>> {
        return db.currentAccountDao().getAllCurrentAccounts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllClientsWithCurrentAccount(): Flow<List<ClientWithCurrentAccount>> {
        return db.currentAccountDao().getAllClientsWithCurrentAccount()
    }

    override fun getClientsWithCurrentAccountByQuery(query: String): Flow<List<ClientWithCurrentAccount>> {
        return db.currentAccountDao().getClientsWithCurrentAccountByQuery(query)
    }

    override suspend fun getClientWithCurrentAccountById(id: Int): ClientWithCurrentAccount {
        return db.currentAccountDao().getClientWithCurrentAccountById(id)
    }

    override suspend fun addCurrentAccount(currentAccount: CurrentAccount) {
        db.currentAccountDao().addCurrentAccount(currentAccount.toEntity())
    }

}