package org.example.project.ui.screens.suppliers

import org.example.project.domain.models.supplier.Supplier

class ClearSupplier {
    fun getClearSupplier(): Supplier = Supplier(
        id = 0,
        name = "",
        mail = "",
        webpage = "",
        phoneNumber = 0L,
        address = "",
        productsOffered = ""
    )
}