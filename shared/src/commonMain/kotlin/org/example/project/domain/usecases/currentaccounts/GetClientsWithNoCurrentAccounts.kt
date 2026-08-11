package org.example.project.domain.usecases.currentaccounts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.example.project.data.db.entities.relations.ClientWithCurrentAccount
import org.example.project.domain.models.client.Client
import org.example.project.domain.repositories.ClientRepository
import org.example.project.domain.repositories.CurrentAccountRepository

class GetClientsWithNoCurrentAccounts(private val repo: CurrentAccountRepository) {

    operator fun invoke(): Flow<List<Client>> {
        return repo.getAllClientsWithCurrentAccount().map { list ->
            list.filter { it.currentAccount == null }
                .map { it.client.toDomain() }
        }
    }

}