package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.promotions.Promotion
import org.example.project.domain.repositories.PromotionRepository

class PromotionRepositoryImpl(private val db: SystemDatabase): PromotionRepository {
    override fun getAllPromotions(): Flow<List<Promotion>> {
        return db.promotionDao().getAllPromotions().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getPromotionById(id: Int): Promotion {
        return db.promotionDao().getPromotionById(id).toDomain()
    }

    override suspend fun addPromotion(promotion: Promotion) {
        db.promotionDao().addPromotion(promotion.toEntity())
    }

    override suspend fun deletePromotionById(id: Int) {
        db.promotionDao().deletePromotionById(id)
    }
}