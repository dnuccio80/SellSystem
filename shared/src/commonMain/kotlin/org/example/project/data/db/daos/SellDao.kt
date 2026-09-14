package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.SellEntity

@Dao
interface SellDao {

    @Query("SELECT * FROM SellEntity")
    fun getAllSells(): Flow<List<SellEntity>>

    @Query("SELECT * FROM SellEntity WHERE description LIKE '%' || :query || '%' ")
    fun getSellsByQuery(query:String):Flow<List<SellEntity>>

    @Query("SELECT * FROM SellEntity WHERE id = :id")
    suspend fun getSellById(id:Int): SellEntity

    @Insert(onConflict = REPLACE)
    suspend fun addSell(sell: SellEntity)

    @Query("DELETE FROM SellEntity WHERE id = :id")
    suspend fun deleteSellById(id:Int)

}