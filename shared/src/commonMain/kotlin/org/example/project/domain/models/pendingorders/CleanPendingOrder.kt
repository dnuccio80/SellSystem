package org.example.project.domain.models.pendingorders

class CleanPendingOrder {
    fun getNew(): PendingOrder {
        return PendingOrder(
            id = 0,
            clientName = "",
            phone = 0L,
            address = "",
            description = "",
            status = PendingOrderState.PENDING,
            priority = PendingOrderPriority.HIGH
        )
    }
}
