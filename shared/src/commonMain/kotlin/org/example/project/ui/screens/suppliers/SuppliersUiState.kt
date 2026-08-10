package org.example.project.ui.screens.suppliers

import org.example.project.domain.models.supplier.Supplier

sealed class SuppliersUiState() {

    data object Loading: SuppliersUiState()
    data class Error(val throwable: Throwable): SuppliersUiState()
    data class Success(
        val query:String,
        val supplierData: Supplier,
        val supplierList:List<Supplier>
    ): SuppliersUiState()
}
