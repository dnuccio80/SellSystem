package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ClientEntity

@Dao
interface ClientDao {

    @Query("SELECT * FROM ClientEntity ORDER BY fullName ASC ")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addClient(client: ClientEntity)

}