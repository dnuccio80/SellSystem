package org.example.project.domain.models.product

import org.example.project.data.db.entities.ProductCategoryEntity

data class ProductCategory(
    val id:Int = 0,
    val name:String
) {
    fun toEntity(): ProductCategoryEntity {
        return ProductCategoryEntity(
            id = id,
            name = name
        )
    }
}
