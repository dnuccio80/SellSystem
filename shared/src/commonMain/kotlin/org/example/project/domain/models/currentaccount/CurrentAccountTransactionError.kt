package org.example.project.domain.models.currentaccount

sealed class CurrentAccountTransactionError(val msg:String): Exception () {
    data object NoTransactionType: CurrentAccountTransactionError("Debes indicar el tipo de transacción")
    data object NoDescription: CurrentAccountTransactionError("Debes indicar la descripción")
    data object NoAmount: CurrentAccountTransactionError("Debes indicar el monto de la transacción")
}