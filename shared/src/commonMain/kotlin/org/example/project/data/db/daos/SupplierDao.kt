package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ClientEntity
import org.example.project.data.db.entities.SupplierEntity

@Dao
interface SupplierDao {

    @Query("SELECT * FROM SupplierEntity ORDER BY name ASC")
    fun getAllSuppliers(): Flow<List<SupplierEntity>>

    @Query("SELECT * FROM SUPPLIERENTITY WHERE name LIKE '%' || :query || '%' ")
    fun getSuppliersByQuery(query:String):Flow<List<SupplierEntity>>

    @Query("SELECT * FROM SupplierEntity WHERE id = :id")
    suspend fun getSupplierById(id:Int): SupplierEntity

    @Insert(onConflict = REPLACE)
    suspend fun addSupplier(supplier: SupplierEntity)

    @Query("DELETE FROM SupplierEntity where id = :id")
    suspend fun deleteSupplierById(id:Int)

}