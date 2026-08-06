package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.ProductCategory

@Entity
data class ProductCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String
) {
    fun toDomain(): ProductCategory {
        return ProductCategory(
            id = id,
            name = name
        )
    }
}
