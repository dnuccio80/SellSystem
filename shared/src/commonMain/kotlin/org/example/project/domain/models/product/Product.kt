package org.example.project.domain.models.product

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.ProductEntity

data class Product(
    val id: Int = 0,
    val name: String,
    val category: String,
    val brand: String,
    val buyPrice: Long,
    val listPrice: Long,
    val cashPrice: Long,
    val manageStock: Boolean,
    val currentStock: Int,
    val adviceStock: Int,
    val description: String,
    val manageExpireDate: Boolean = false,
    val expireDate: LocalDate? = null,
    val imagePath: String? = null,
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(
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
