package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM ProductEntity ORDER By name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE name LIKE '%' || :name || '%'")
    fun getProductBySearch(name: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity

    @Insert(onConflict = REPLACE)
    suspend fun addProduct(productEntity: ProductEntity)

    @Query("DELETE FROM ProductEntity WHERE id = :id")
    suspend fun deleteProductById(id:Int)




}