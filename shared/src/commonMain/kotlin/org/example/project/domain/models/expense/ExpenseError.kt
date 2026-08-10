package org.example.project.domain.models.expense

sealed class ExpenseError(val msg:String): Exception () {
    data object EmptyDescription: ExpenseError("Debes ingresar la descripción del gasto")
    data object EmptyAmount: ExpenseError("Debes ingresar el monto del gasto")
}