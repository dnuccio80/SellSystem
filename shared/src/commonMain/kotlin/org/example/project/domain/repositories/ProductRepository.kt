package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.product.ProductWithPromo

interface ProductRepository {
    fun getAllProducts(): Flow<List<ProductWithPromo>>
    fun getProductsByQuery(query:String):Flow<List<ProductWithPromo>>
    suspend fun updateProduct(product: ProductWithPromo)
    suspend fun getProductById(id:Int): ProductWithPromo
    suspend fun addProduct(product: ProductWithPromo)
    suspend fun deleteProductById(id:Int)
}