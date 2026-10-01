package org.example.project.ui.models

import kotlinx.datetime.LocalDate
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.promotions.PromotionCategory

data class PromotionPresentation(
    val buyX:Int,
    val payY:Int,
    val percent:Int,
    val promoteBy: PromotionCategory,
    val brandSelected:String? = null,
    val categoryListSelected: List<ProductCategory>? = null,
    val specificProducts:List<Product>? = null,
    val date: LocalDate? = null,
)