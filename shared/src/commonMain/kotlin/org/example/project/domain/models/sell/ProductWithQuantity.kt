package org.example.project.domain.models.sell

import org.example.project.domain.models.product.Product

data class ProductWithQuantity(
    val product: Product,
    val quantity: Int
)
