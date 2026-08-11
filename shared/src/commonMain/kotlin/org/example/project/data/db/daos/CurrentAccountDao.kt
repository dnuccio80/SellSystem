package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.CurrentAccountEntity
import org.example.project.data.db.entities.relations.ClientWithCurrentAccount

@Dao
interface CurrentAccountDao {

    @Query("SELECT * FROM CurrentAccountEntity")
    fun getAllCurrentAccounts(): Flow<List<CurrentAccountEntity>>

    @Transaction
    @Query("SELECT * FROM ClientEntity ORDER by fullName ASC")
    fun getAllClientsWithCurrentAccount(): Flow<List<ClientWithCurrentAccount>>

    @Transaction
    @Query("SELECT * FROM ClientEntity WHERE fullName LIKE '%' || :query || '%'")
    fun getClientsWithCurrentAccountByQuery(query:String):Flow<List<ClientWithCurrentAccount>>

    @Transaction
    @Query("SELECT * FROM ClientEntity WHERE id = :id")
    suspend fun getClientWithCurrentAccountById(id:Int): ClientWithCurrentAccount

    @Insert(onConflict = REPLACE)
    suspend fun addCurrentAccount(currentAccountEntity: CurrentAccountEntity)


}