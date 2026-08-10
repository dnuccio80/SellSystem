package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.supplier.Supplier
import org.example.project.domain.repositories.SupplierRepository

class SupplierRepositoryImpl(val db: SystemDatabase): SupplierRepository {
    override fun getAllSuppliers(): Flow<List<Supplier>> {
        return db.supplierDao().getAllSuppliers().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getSuppliersByQuery(query: String): Flow<List<Supplier>> {
        return db.supplierDao().getSuppliersByQuery(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getSupplierById(id: Int): Supplier {
        return db.supplierDao().getSupplierById(id).toDomain()
    }

    override suspend fun addSupplier(supplier: Supplier) {
        db.supplierDao().addSupplier(supplier.toEntity())
    }

    override suspend fun deleteSupplierById(id: Int) {
        db.supplierDao().deleteSupplierById(id)
    }
}