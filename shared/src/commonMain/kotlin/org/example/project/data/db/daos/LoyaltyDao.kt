package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.relations.LoyaltyEntity

@Dao
interface LoyaltyDao {

    @Query("SELECT * FROM LoyaltyEntity")
    fun getAllLoyaltyAccounts(): Flow<List<LoyaltyEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addLoyaltyAccount(loyaltyEntity: LoyaltyEntity)

    @Query("DELETE FROM LoyaltyEntity WHERE clientId = :clientId")
    suspend fun deleteLoyaltyAccountFromClientId(clientId:Int)

}