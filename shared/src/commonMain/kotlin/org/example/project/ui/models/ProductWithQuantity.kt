package org.example.project.ui.models

import org.example.project.domain.models.product.Product
import org.example.project.domain.models.sell.ProductWithQuantity

data class ProductWithQuantityPresentation(
    val product: Product,
    val quantity:Int,
    val amount:Long
) {
    fun toDomain(): ProductWithQuantity {
        return ProductWithQuantity(
            product = product,
            quantity = quantity
        )
    }
}