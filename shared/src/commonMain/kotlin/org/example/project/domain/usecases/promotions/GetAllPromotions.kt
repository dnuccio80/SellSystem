package org.example.project.domain.usecases.promotions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.models.promotions.Promotion
import org.example.project.domain.models.promotions.PromotionCategory
import org.example.project.domain.repositories.PromotionRepository
import org.example.project.ui.models.PromotionActive
import kotlin.time.Clock
import kotlin.time.Instant

class GetAllPromotions(private val repository: PromotionRepository, private val getProductsForPromotions: GetProductsForPromotions, private val getCategoriesForPromotions: GetCategoriesForPromotions) {

    operator fun invoke(): Flow<List<PromotionActive>> {

        return repository.getAllPromotions().map { list ->
            list.map { promotion ->
                when(promotion.promotionBy) {
                    PromotionCategory.BRAND -> {
                        PromotionActive(
                            promotion = promotion.toPresentation(),
                            isActive = getIsActive(promotion)
                        )
                    }
                    PromotionCategory.CATEGORY -> {
                        PromotionActive(
                            promotion = promotion.toPresentation(categoryList = getCategoriesForPromotions(promotion.categoryListIds.orEmpty())),
                            isActive = getIsActive(promotion)
                        )
                    }
                    PromotionCategory.SPECIFIC -> {
                        PromotionActive(
                            promotion = promotion.toPresentation(specificProducts =  getProductsForPromotions(promotion.productListIds.orEmpty())),
                            isActive = getIsActive(promotion)
                        )
                    }
                }
            }.sortedByDescending { it.isActive }

        }
    }

}

private fun getIsActive(promotion: Promotion): Boolean {

    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return when {
        promotion.promoStart == null && promotion.promoEnd == null -> true
        promotion.promoStart == null && promotion.promoEnd != null && promotion.promoEnd > now -> true
        promotion.promoStart != null && promotion.promoStart < now && promotion.promoEnd == null -> true
        promotion.promoStart != null && promotion.promoStart < now && promotion.promoEnd != null && promotion.promoEnd > now -> true
        else -> false
    }

}