package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.loyalty.Loyalty

interface LoyaltyRepository {
    fun getAllLoyaltyAccounts(): Flow<List<Loyalty>>
    suspend fun addLoyaltyAccount(loyalty: Loyalty)
    suspend fun deleteLoyaltyAccountFromClientId(clientId:Int)
}