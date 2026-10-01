package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.project.domain.models.promotions.Promotion
import org.example.project.domain.models.promotions.PromotionCategory
import org.example.project.domain.models.promotions.PromotionType

@Entity
data class PromotionEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val promotionType:String,
    val promotionBy:String,
    val date: LocalDate?
){
    fun toDomain(): Promotion {
        return Promotion(
            id = id,
            promotionType = getPromotionType(promotionType),
            promotionBy = getPromotionBy(promotionBy),
            date = date
        )
    }
}

fun getPromotionType(title:String): PromotionType {
    return PromotionType.entries.first { it.title == title }
}

fun getPromotionBy(title:String): PromotionCategory {
    return PromotionCategory.entries.first { it.title == title }
}
