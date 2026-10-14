package org.example.project.domain.models.promotions

import kotlinx.datetime.LocalDateTime
import org.example.project.data.db.entities.PromotionEntity
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.ui.models.PromotionPresentation


enum class PromotionType(val title: String) {
    BUY_X_PAY_Y("Compra X, paga Y"), PERCENT("Por porcentaje")
}

enum class PromotionCategory(val title: String) {
    BRAND("Marca"), CATEGORY("Categoría"), SPECIFIC("Productos específicos")
}

data class Promotion(
    val id:Int = 0,
    val promotionType: PromotionType,
    val promotionBy: PromotionCategory,
    val buyX:Int? = null,
    val payY:Int? = null,
    val percent:Int? = null,
    val productListIds:List<Int>? = null,
    val categoryListIds:List<Int>? = null,
    val brandList:List<String>? = null,
    val promoStart: LocalDateTime? = null,
    val promoEnd: LocalDateTime? = null
) {
    fun toEntity(): PromotionEntity {
        return PromotionEntity(
            id = id,
            promotionType = promotionType.title,
            promotionBy = promotionBy.title,
            buyX = buyX,
            payY = payY,
            percent = percent,
            productListIds = productListIds,
            categoryListIds = categoryListIds,
            brandList = brandList,
            promoStart = promoStart,
            promoEnd = promoEnd
        )
    }

    fun toPresentation(specificProducts:List<Product>? = null, categoryList:List<ProductCategory>? = null): PromotionPresentation {
        return PromotionPresentation(
            id = id,
            buyX = buyX,
            payY = payY,
            percent = percent,
            promoteBy = promotionBy,
            promotionType = promotionType,
            brandSelected = brandList?.joinToString(","),
            categoryListSelected = categoryList,
            specificProducts = specificProducts,
            hasDate = promoStart != null || promoEnd != null,
            hasDateInit = promoStart != null,
            hasDateEnd = promoEnd != null,
            promoStart = promoStart,
            promoEnd = promoEnd
        )
    }
}