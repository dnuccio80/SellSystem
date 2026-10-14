package org.example.project.domain.usecases.promotions

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.promotions.Promotion
import org.example.project.domain.models.promotions.PromotionCategory.BRAND
import org.example.project.domain.models.promotions.PromotionCategory.CATEGORY
import org.example.project.domain.models.promotions.PromotionCategory.SPECIFIC
import org.example.project.domain.models.promotions.PromotionType
import kotlin.time.Clock

class GetPromotionsForProduct(
    private val getCategoriesForPromotions: GetCategoriesForPromotions,
) {

    suspend operator fun invoke(product: Product, promotions: List<Promotion>): Promotion? {

        val now = Clock.System.now()

        val promoList = promotions.filter { promo ->
            when (promo.promotionBy) {
                BRAND -> {
                    promo.brandList.orEmpty()
                        .any { it.trim().equals(product.brand.trim(), ignoreCase = true) }
                }

                CATEGORY -> {
                    val categoryList: List<ProductCategory> = getCategoriesForPromotions(promo.categoryListIds.orEmpty())
                    categoryList.any { it.name == product.category }
                }

                SPECIFIC -> {
                    product.id in promo.productListIds.orEmpty()
                }
            }
        }.filter { promo -> getPromoIsActive(promo) }

        val buyXPayYPromos: List<Promotion> = promoList.filter { promo ->
            promo.promotionType == PromotionType.BUY_X_PAY_Y
        }

        if(buyXPayYPromos.isNotEmpty()) {
            return buyXPayYPromos.maxBy { it.buyX ?: 0 }
        }

        val percentPromos = promoList.filter {
            it.promotionType == PromotionType.PERCENT
        }

        return percentPromos.maxByOrNull {
            it.percent ?: 0
        }

    }

}

private fun getPromoIsActive(promotion: Promotion): Boolean {

    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    return when {
        promotion.promoStart == null && promotion.promoEnd == null -> true
        promotion.promoStart == null && promotion.promoEnd != null && promotion.promoEnd > now -> true
        promotion.promoStart != null && promotion.promoStart < now && promotion.promoEnd == null -> true
        promotion.promoStart != null && promotion.promoStart < now && promotion.promoEnd != null && promotion.promoEnd > now -> true
        else -> false
    }

}