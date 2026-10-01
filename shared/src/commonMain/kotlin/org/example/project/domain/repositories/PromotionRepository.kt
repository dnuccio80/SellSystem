package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.promotions.Promotion

interface PromotionRepository {
    fun getAllPromotions(): Flow<List<Promotion>>
    suspend fun getPromotionById(id:Int): Promotion
    suspend fun addPromotion(promotion: Promotion)
    suspend fun deletePromotionById(id:Int)
}