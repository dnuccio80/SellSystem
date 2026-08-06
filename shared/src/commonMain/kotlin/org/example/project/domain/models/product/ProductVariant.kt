package org.example.project.domain.models.product

import org.example.project.data.db.entities.ProductVariantEntity

data class ProductVariant(
    val id: Int = 0,
    val name: String,
    val variants: List<String>,
) {
    fun toEntity(): ProductVariantEntity {
        return ProductVariantEntity(
            id = id,
            name = name,
            variants = variants
        )
    }
}
