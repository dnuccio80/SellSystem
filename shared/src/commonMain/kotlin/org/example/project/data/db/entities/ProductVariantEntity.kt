package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.ProductVariant

@Entity
data class ProductVariantEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val variants: String
) {
    fun toDomain(): ProductVariant {
        return ProductVariant(
            id = id,
            name = name,
            variants = variants
        )
    }
}
