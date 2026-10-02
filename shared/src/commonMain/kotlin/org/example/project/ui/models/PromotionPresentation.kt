package org.example.project.ui.models

import kotlinx.datetime.LocalDate
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.promotions.PromotionCategory
import org.example.project.domain.models.promotions.PromotionType

data class PromotionPresentation(
    val buyX:Int? = null,
    val payY:Int? = null,
    val percent:Int? = null,
    val promoteBy: PromotionCategory,
    val promotionType: PromotionType,
    val brandSelected:String? = null,
    val categoryListSelected: List<ProductCategory>? = null,
    val specificProducts:List<Product>? = null,
    val hasDate: Boolean = false,
    val date: LocalDate? = null,
)