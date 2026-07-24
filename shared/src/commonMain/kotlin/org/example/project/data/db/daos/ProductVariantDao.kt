package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.ProductVariantEntity
import org.example.project.domain.models.ProductVariant

@Dao
interface ProductVariantDao {
    @Query("SELECT * FROM ProductVariantEntity ORDER BY name ASC")
    fun getAllProductVariants(): Flow<List<ProductVariantEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addProductVariant(productVariant: ProductVariantEntity)

    @Query("DELETE FROM ProductVariantEntity WHERE id = :id")
    suspend fun deleteProductVariantById(id:Int)


}