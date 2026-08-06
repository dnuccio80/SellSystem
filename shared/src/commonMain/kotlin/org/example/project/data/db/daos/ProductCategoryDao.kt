package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ProductCategoryEntity

@Dao
interface ProductCategoryDao {

    @Query("SELECT * FROM ProductCategoryEntity ORDER BY name ASC")
    fun getAllCategories(): Flow<List<ProductCategoryEntity>>

    @Query("SELECT * FROM ProductCategoryEntity WHERE id = :id")
    suspend fun getCategoryById(id:Int): ProductCategoryEntity

    @Query("SELECT * FROM ProductCategoryEntity WHERE name LIKE '%' || :query || '%' ")
    fun getCategoriesByQuery(query:String):Flow<List<ProductCategoryEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addCategory(productCategoryEntity: ProductCategoryEntity)

    @Query("DELETE FROM ProductCategoryEntity WHERE id = :id")
    suspend fun deleteCategory(id:Int)

}