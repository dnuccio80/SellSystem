package org.example.project.domain.models.promotions

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.PromotionEntity


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
    val date: LocalDate? = null
) {
    fun toEntity(): PromotionEntity {
        return PromotionEntity(
            id = id,
            promotionType = promotionType.title,
            promotionBy = promotionBy.title,
            date = date
        )
    }
}