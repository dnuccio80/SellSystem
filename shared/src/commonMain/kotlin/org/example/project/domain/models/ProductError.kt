package org.example.project.domain.models

sealed class ProductError(val msg: String): Exception() {
    data object ListPriceLessThanBuyPrice: ProductError("El precio de lista es menor al precio de compra")
    data object CashPriceMoreThenListPrice: ProductError("El precio en efectivo/transferencia deberia ser menor al precio de lista")
    data object CashPriceLessThanBuyPrice: ProductError("El precio en efectivo/transferencia es menor al precio de compra!")
    data object NotEnoughData: ProductError("Falta ingresar nombre o marca del producto")
    data object NoCashPriceData: ProductError("Debes ingresar el precio en efectivo/transferencia")
    data object NoListPriceData: ProductError("Debes ingresar el precio de lista")
    data object NoBuyPriceData: ProductError("Debes ingresar el precio de compra")
    data object InvalidStockData: ProductError("Si manejas el stock, no debe ser cero")
    data object NotExpireDateSelected: ProductError("Si deseas gestionar la fecha de vencimiento, tenes que agregar una")


}