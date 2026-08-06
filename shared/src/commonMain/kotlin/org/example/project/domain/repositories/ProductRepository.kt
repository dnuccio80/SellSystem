package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.product.Product

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByQuery(query:String):Flow<List<Product>>
    suspend fun getProductById(id:Int): Product
    suspend fun addProduct(product: Product)
    suspend fun deleteProductById(id:Int)
}