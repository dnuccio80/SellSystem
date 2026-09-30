package org.example.project.domain.usecases.pendingorders

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.models.pendingorders.PendingOrder
import org.example.project.domain.models.pendingorders.PendingOrderState
import org.example.project.domain.repositories.PendingOrdersRepository
import org.example.project.domain.usecases.pendingorders.PendingOrderTarget.ACTIVE
import org.example.project.domain.usecases.pendingorders.PendingOrderTarget.DONE

enum class PendingOrderTarget(val etiquette: String) {
    ACTIVE("Activos"), DONE("Completados")
}

class GetPendingOrders(private val pendingOrdersRepository: PendingOrdersRepository) {

    operator fun invoke(clientName:String, filter: PendingOrderTarget): Flow<List<PendingOrder>> {

        val listByQuery = if (clientName.isBlank()) {
            pendingOrdersRepository.getAllPendingOrders()
        } else {
            pendingOrdersRepository.getPendingOrderByClientName(clientName)
        }

        return when(filter) {
            ACTIVE -> listByQuery.map { list -> list.filter { it.status != PendingOrderState.COMPLETED }.sortedBy { it.priority.ordinal } }
            DONE -> listByQuery.map { list -> list.filter { it.status == PendingOrderState.COMPLETED }.sortedBy { it.priority.ordinal } }
        }
    }

}