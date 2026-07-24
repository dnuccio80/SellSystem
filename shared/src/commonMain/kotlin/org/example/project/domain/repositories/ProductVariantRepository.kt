package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.ProductVariant

interface ProductVariantRepository {

    fun getAllProductVariants(): Flow<List<ProductVariant>>
    suspend fun addProductVariant(productVariant: ProductVariant)
    suspend fun deleteProductVariantById(id:Int)
}