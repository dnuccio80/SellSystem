package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.project.domain.models.product.Product

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val category:String,
    val brand:String,
    val buyPrice:Long,
    val listPrice: Long,
    val cashPrice:Long,
    val currentStock:Int,
    val adviceStock:Int,
    val manageStock: Boolean,
    val description:String,
    val expireDate: LocalDate?,
    val imagePath:String? = null
) {
    fun toDomain(): Product {
        return Product(
            id = id,
            name = name,
            category = category,
            brand = brand,
            buyPrice = buyPrice,
            listPrice = listPrice,
            cashPrice = cashPrice,
            currentStock = currentStock,
            adviceStock = adviceStock,
            description = description,
            manageStock = manageStock,
            expireDate = expireDate,
            imagePath = imagePath
        )
    }
}
