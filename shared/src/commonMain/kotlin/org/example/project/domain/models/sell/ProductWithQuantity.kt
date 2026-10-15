package org.example.project.domain.models.sell

import org.example.project.domain.models.product.ProductWithPromotion

data class ProductWithQuantity(
    val productWithPromo: ProductWithPromotion,
    val quantity: Int
)
