package org.example.project.domain.models.product

import org.example.project.domain.models.promotions.Promotion

data class ProductWithPromotion(
    val product: ProductWithPromo,
    val promotion: Promotion?
)
