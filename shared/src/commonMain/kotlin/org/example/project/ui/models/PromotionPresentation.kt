package org.example.project.ui.models

import kotlinx.datetime.LocalDateTime
import org.example.project.domain.models.product.ProductWithPromo
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.promotions.Promotion
import org.example.project.domain.models.promotions.PromotionCategory
import org.example.project.domain.models.promotions.PromotionType

data class PromotionPresentation(
    val id: Int = 0,
    val buyX: Int? = null,
    val payY: Int? = null,
    val percent: Int? = null,
    val promoteBy: PromotionCategory,
    val promotionType: PromotionType,
    val brandSelected: String? = null,
    val categoryListSelected: List<ProductCategory>? = null,
    val specificProducts: List<ProductWithPromo>? = null,
    val hasDate: Boolean = false,
    val hasDateInit: Boolean = true,
    val hasDateEnd: Boolean = true,
    val promoStart: LocalDateTime? = null,
    val promoEnd: LocalDateTime? = null,
) {
    fun toDomain(): Promotion {

        return Promotion(
            id = id,
            promotionType = promotionType,
            promotionBy = promoteBy,
            buyX = buyX,
            payY = payY,
            percent = percent,
            productListIds = specificProducts?.map { it.id },
            categoryListIds = categoryListSelected?.map { it.id },
            brandList = brandSelected?.trim()?.split(","),
            promoStart = promoStart,
            promoEnd = promoEnd
        )
    }
}