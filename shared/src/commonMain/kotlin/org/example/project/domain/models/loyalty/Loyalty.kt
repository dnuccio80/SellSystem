package org.example.project.domain.models.loyalty

import org.example.project.data.db.entities.relations.LoyaltyEntity

data class Loyalty(
    val id:Int = 0,
    val clientId:Int,
    val points:Int
) {
    fun toEntity(): LoyaltyEntity {
        return LoyaltyEntity (
            id = id,
            clientId = clientId,
            points = points
        )
    }
}
