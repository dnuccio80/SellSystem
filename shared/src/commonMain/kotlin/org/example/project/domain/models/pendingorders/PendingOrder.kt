package org.example.project.domain.models.pendingorders

import org.example.project.data.db.entities.PendingOrderEntity


enum class PendingOrderState(val etiquette: String) {
    PENDING("Pendiente"), CONFIRMED("Confirmado"), PREPARING("Preparando"), SENT("Despachado"), CANCELED(
        "Cancelado"
    ),
    COMPLETED("Completado")
}

enum class PendingOrderPriority(val etiquette: String) {
    HIGH("Alta"), MID("Media"), LOW("Baja")
}


data class PendingOrder(
    val id:Int = 0,
    val clientName:String,
    val phone:Long,
    val address:String,
    val description:String,
    val status: PendingOrderState,
    val priority: PendingOrderPriority
) {
    fun toEntity(): PendingOrderEntity {
        return PendingOrderEntity(
            id = id,
            clientName = clientName,
            phone = phone,
            address = address,
            description = description,
            status = status.etiquette,
            priority = priority.etiquette
        )
    }
}
