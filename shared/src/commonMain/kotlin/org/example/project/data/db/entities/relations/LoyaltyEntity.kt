package org.example.project.data.db.entities.relations

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.loyalty.Loyalty

@Entity
data class LoyaltyEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val clientId:Int,
    val points:Int,
) {
    fun toDomain(): Loyalty {
        return Loyalty(
            id = id,
            clientId = clientId,
            points = points
        )
    }
}
