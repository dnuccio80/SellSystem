package org.example.project.domain.usecases.suppliers

import org.example.project.domain.models.supplier.Supplier
import org.example.project.domain.models.supplier.SupplierError
import org.example.project.domain.models.supplier.SupplierError.*
import org.example.project.domain.repositories.SupplierRepository

class AddSupplier(private val repository: SupplierRepository) {

    suspend operator fun invoke(supplier: Supplier) {
        when {
            supplier.name.isBlank() -> throw EmptyName
            supplier.phoneNumber == 0L && supplier.mail.isBlank() -> throw NoContactData
        }
        repository.addSupplier(supplier)
    }

}