package org.example.project.domain.repositories

import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.SupplierEntity
import org.example.project.domain.models.supplier.Supplier

interface SupplierRepository {
    fun getAllSuppliers(): Flow<List<Supplier>>
    fun getSuppliersByQuery(query:String):Flow<List<Supplier>>
    suspend fun getSupplierById(id:Int): Supplier
    suspend fun addSupplier(supplier: Supplier)
    suspend fun deleteSupplierById(id:Int)
}