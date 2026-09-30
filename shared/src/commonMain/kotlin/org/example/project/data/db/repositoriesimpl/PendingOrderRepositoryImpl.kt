package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.pendingorders.PendingOrder
import org.example.project.domain.repositories.PendingOrdersRepository

class PendingOrderRepositoryImpl(private val db: SystemDatabase): PendingOrdersRepository {
    override fun getAllPendingOrders(): Flow<List<PendingOrder>> {
        return db.pendingOrderDao().getAllPendingOrders().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getPendingOrderByClientName(clientName: String): Flow<List<PendingOrder>> {
        return db.pendingOrderDao().getPendingOrderByClientName(clientName).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getPendingOrderById(id: Int): PendingOrder {
        return db.pendingOrderDao().getPendingOrderById(id).toDomain()
    }

    override suspend fun addPendingOrder(pendingOrder: PendingOrder) {
        db.pendingOrderDao().addPendingOrder(pendingOrder.toEntity())
    }

    override suspend fun deletePendingOrder(id: Int) {
        db.pendingOrderDao().deletePendingOrder(id)
    }
}