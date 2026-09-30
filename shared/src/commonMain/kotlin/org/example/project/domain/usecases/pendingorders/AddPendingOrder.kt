package org.example.project.domain.usecases.pendingorders

import org.example.project.domain.models.pendingorders.PendingOrder
import org.example.project.domain.models.pendingorders.PendingOrderError
import org.example.project.domain.repositories.PendingOrdersRepository

class AddPendingOrder(private val pendingOrdersRepository: PendingOrdersRepository) {

    suspend operator fun invoke(pendingOrder: PendingOrder) {

        when {
            pendingOrder.clientName.isBlank() -> throw PendingOrderError.NoClientName
            pendingOrder.phone == 0L -> throw PendingOrderError.NoPhone
            pendingOrder.address.isBlank() -> throw PendingOrderError.NoAddress
            pendingOrder.description.isBlank() -> throw PendingOrderError.EmptyDescription
        }

        pendingOrdersRepository.addPendingOrder(pendingOrder)

    }

}