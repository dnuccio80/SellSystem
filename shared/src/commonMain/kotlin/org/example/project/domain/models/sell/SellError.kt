package org.example.project.domain.models.sell

import org.example.project.domain.models.expense.ExpenseError

sealed class SellError(val msg: String) : Exception() {
    data object NoItems : SellError("Debes agregar items vendidos.")
    data object CurrentAccountButNotSelected :
        SellError("Debes seleccionar un cliente con cuenta corriente.")

    data object UsualClientButNotSelected :
        SellError("Si seleccionas 'Cliente usual', debes seleccionar uno.")
}
