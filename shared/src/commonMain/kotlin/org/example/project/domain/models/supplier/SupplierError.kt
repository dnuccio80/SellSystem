package org.example.project.domain.models.supplier

import org.example.project.domain.models.product.ProductError

sealed class SupplierError(val msg: String): Exception() {
    data object EmptyName: SupplierError("El nombre del proveedor es obligatorio")
    data object NoContactData: SupplierError("Tenes que agregar al menos un mail o un número de teléfono")
}

