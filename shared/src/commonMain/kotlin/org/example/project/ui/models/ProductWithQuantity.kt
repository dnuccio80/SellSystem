package org.example.project.ui.models

import org.example.project.domain.models.product.Product

data class ProductWithQuantity(
    val product: Product,
    val quantity:Int,
)
