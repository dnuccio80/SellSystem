package org.example.project.domain.usecases.suppliers

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.supplier.Supplier
import org.example.project.domain.repositories.SupplierRepository

class GetSuppliers(private val repository: SupplierRepository) {

    operator fun invoke(query:String): Flow<List<Supplier>> {
        return if(query.isNotBlank()) {
            repository.getSuppliersByQuery(query)
        }else {
            repository.getAllSuppliers()
        }
    }

}