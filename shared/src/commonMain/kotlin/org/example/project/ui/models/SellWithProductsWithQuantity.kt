package org.example.project.ui.models

import org.example.project.domain.models.sell.ProductWithQuantity
import org.example.project.domain.models.sell.Sell

data class SellWithProductsWithQuantity(
    val sell: Sell,
    val productWithQuantity: List<ProductWithQuantity>,
)
