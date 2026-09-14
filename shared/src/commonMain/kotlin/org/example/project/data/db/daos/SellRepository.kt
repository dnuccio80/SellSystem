package org.example.project.data.db.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.SellEntity
import org.example.project.domain.models.sell.Sell

interface SellRepository {
    fun getAllSells(): Flow<List<Sell>>
    fun getSellsByQuery(query:String):Flow<List<Sell>>
    suspend fun getSellById(id:Int): Sell
    suspend fun addSell(sell: Sell)
    suspend fun deleteSellById(id:Int)
}