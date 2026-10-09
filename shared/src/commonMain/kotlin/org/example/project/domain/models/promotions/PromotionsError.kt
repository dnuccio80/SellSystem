package org.example.project.domain.models.promotions

sealed class PromotionsError(val msg: String): Exception() {
    data object NoBuyAmount: PromotionsError("Debes agregar la cantidad de items que compra")
    data object NoPayAmount: PromotionsError("Debes agregar la cantidad de items que paga")
    data object PayMoreThanBuy: PromotionsError("La cantidad que compra debe ser mayor que la que paga!")
    data object NoPercentAmount: PromotionsError("Debes indicar el porcentaje de descuento")
    data object NoBrandsAdded: PromotionsError("Debes agregar las marcas que participan del descuento")
    data object NoCategoriesAdded: PromotionsError("Debes agregar las categorías que participan del descuento")
    data object NoSpecificProductsAdded: PromotionsError("Debes agregar los productos que participan del descuento")
    data object NoPromoDataInit: PromotionsError("Debes indicar la fecha de inicio de la promoción")
    data object NoPromoDataEnd: PromotionsError("Debes indicar la fecha de finalización de la promoción")
    data object NoPromoDataInitAndEndWithHasDateSelected: PromotionsError("Debes colocar fecha y hora de inicio y/o fecha y hora de fin, sino desclickea la opción")
    data object IncompatiblePromoDateInitEnd: PromotionsError("La fecha y hora de inicio no puede ser luego de la fecha y hora de finalización")
}
