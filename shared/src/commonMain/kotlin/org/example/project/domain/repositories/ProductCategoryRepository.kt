package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.product.ProductCategory

interface ProductCategoryRepository {
    fun getAllCategories(): Flow<List<ProductCategory>>
    suspend fun getCategoryById(id:Int): ProductCategory
    fun getCategoriesByQuery(query:String):Flow<List<ProductCategory>>
    suspend fun addCategory(productCategory: ProductCategory)
    suspend fun deleteCategory(id:Int)
}