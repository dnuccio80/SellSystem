package org.example.project.domain.usecases.currentaccounts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.example.project.data.db.entities.relations.ClientWithCurrentAccount
import org.example.project.domain.repositories.CurrentAccountRepository

class GetCurrentAccounts(private val currentAccountRepo: CurrentAccountRepository) {

    operator fun invoke(query:String): Flow<List<ClientWithCurrentAccount>> {
        return if (query.isNotBlank()) {
            currentAccountRepo.getClientsWithCurrentAccountByQuery(query)
        } else {
            currentAccountRepo.getAllClientsWithCurrentAccount().map { list ->
                list.filter { it.currentAccount != null }
            }
        }
    }

}