package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.pendingorders.PendingOrder

interface PendingOrdersRepository {
    fun getAllPendingOrders(): Flow<List<PendingOrder>>
    fun getPendingOrderByClientName(clientName:String):Flow<List<PendingOrder>>
    suspend fun getPendingOrderById(id: Int): PendingOrder
    suspend fun addPendingOrder(pendingOrder: PendingOrder)
    suspend fun deletePendingOrder(id:Int)
}