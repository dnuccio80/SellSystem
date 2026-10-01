package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.PromotionEntity

@Dao
interface PromotionDao {

    @Query("SELECT * FROM PromotionEntity")
    fun getAllPromotions(): Flow<List<PromotionEntity>>

    @Query("SELECT * FROM PromotionEntity WHERE id = :id")
    suspend fun getPromotionById(id:Int): PromotionEntity

    @Insert(onConflict = REPLACE)
    suspend fun addPromotion(promotion: PromotionEntity)

    @Query("DELETE FROM PromotionEntity WHERE id = :id")
    suspend fun deletePromotionById(id:Int)
}