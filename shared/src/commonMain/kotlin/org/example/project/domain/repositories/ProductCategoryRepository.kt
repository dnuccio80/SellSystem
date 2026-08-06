package org.example.project.domain.repositories

import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ProductCategoryEntity
import org.example.project.domain.models.ProductCategory

interface ProductCategoryRepository {
    fun getAllCategories(): Flow<List<ProductCategory>>
    suspend fun getCategoryById(id:Int): ProductCategory
    fun getCategoriesByQuery(query:String):Flow<List<ProductCategory>>
    suspend fun addCategory(productCategory: ProductCategory)
    suspend fun deleteCategory(id:Int)
}