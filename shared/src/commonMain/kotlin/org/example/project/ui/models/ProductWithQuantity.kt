package org.example.project.ui.models

import org.example.project.domain.models.product.ProductWithPromotion
import org.example.project.domain.models.sell.ProductWithQuantity

data class ProductWithQuantityPresentation(
    val productWithPromo: ProductWithPromotion,
    val quantity:Int,
    val amount:Long
) {
    fun toDomain(): ProductWithQuantity {
        return ProductWithQuantity(
            productWithPromo = productWithPromo,
            quantity = quantity
        )
    }
}