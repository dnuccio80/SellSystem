package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.PendingOrderEntity
import org.example.project.domain.models.pendingorders.PendingOrder

@Dao
interface PendingOrderDao {

    @Query("SELECT * FROM PendingOrderEntity")
    fun getAllPendingOrders(): Flow<List<PendingOrderEntity>>

    @Query("SELECT * FROM PendingOrderEntity WHERE id = :id")
    suspend fun getPendingOrderById(id:Int): PendingOrderEntity

    @Query("SELECT * FROM PendingOrderEntity WHERE clientName LIKE '%' || :clientName || '%'")
    fun getPendingOrderByClientName(clientName:String):Flow<List<PendingOrderEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addPendingOrder(order: PendingOrderEntity)

    @Query("DELETE FROM PendingOrderEntity WHERE id = :id")
    suspend fun deletePendingOrder(id:Int)



}