package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.pendingorders.PendingOrder
import org.example.project.domain.models.pendingorders.PendingOrderPriority
import org.example.project.domain.models.pendingorders.PendingOrderState

@Entity
data class PendingOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val clientName:String,
    val phone:Long,
    val address:String,
    val description:String,
    val status:String,
    val priority: String
) {
    fun toDomain(): PendingOrder {
        return PendingOrder(
            id = id,
            clientName = clientName,
            phone = phone,
            address = address,
            description = description,
            status = getStatus(status),
            priority = getPriority(priority)
        )
    }
}

private fun getStatus(etiquette:String): PendingOrderState {
    return PendingOrderState.entries.first { it.etiquette == etiquette }
}

private fun getPriority(etiquette: String): PendingOrderPriority {
    return PendingOrderPriority.entries.first { it.etiquette == etiquette }
}