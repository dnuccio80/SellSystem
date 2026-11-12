package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.loyalty.Loyalty
import org.example.project.domain.repositories.LoyaltyRepository

class LoyaltyRepositoryImpl(private val db: SystemDatabase): LoyaltyRepository {
    override fun getAllLoyaltyAccounts(): Flow<List<Loyalty>> {
        return db.loyaltyDao().getAllLoyaltyAccounts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addLoyaltyAccount(loyalty: Loyalty) {
        db.loyaltyDao().addLoyaltyAccount(loyalty.toEntity())
    }

    override suspend fun deleteLoyaltyAccountFromClientId(clientId: Int) {
        db.loyaltyDao().deleteLoyaltyAccountFromClientId(clientId)
    }
}