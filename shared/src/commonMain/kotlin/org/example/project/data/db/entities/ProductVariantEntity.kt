package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.product.ProductVariant

@Entity
data class ProductVariantEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val variants: List<String>
) {
    fun toDomain(): ProductVariant {
        return ProductVariant(
            id = id,
            name = name,
            variants = variants
        )
    }
}
