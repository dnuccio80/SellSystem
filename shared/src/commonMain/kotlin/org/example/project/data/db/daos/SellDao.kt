package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.SellEntity
import org.example.project.data.db.entities.relations.SellProductEntity

@Dao
interface SellDao {

    @Query("SELECT * FROM SellEntity")
    fun getAllSells(): Flow<List<SellEntity>>

    @Query("SELECT * FROM SellEntity WHERE paymentMethod LIKE '%' || :query || '%' ")
    fun getSellsByQuery(query:String):Flow<List<SellEntity>>

    @Insert
    suspend fun addSellProducts(products:List<SellProductEntity>)

    @Query("SELECT * FROM SellProductEntity WHERE sellId = :sellId")
    suspend fun getProductsFromSell(sellId:Int):List<SellProductEntity>

    @Query("SELECT * FROM SellEntity WHERE id = :id")
    suspend fun getSellById(id:Int): SellEntity

    @Transaction
    suspend fun addSellWithProducts(
        sell: SellEntity,
        products:List<SellProductEntity>
    ) {

        val sellId = addSell(sell)

        val productsWithSellId = products.map { productEntity ->
            productEntity.copy(sellId = sellId.toInt())
        }

        addSellProducts(productsWithSellId)
    }

    @Insert(onConflict = REPLACE)
    suspend fun addSell(sell: SellEntity):Long

    @Query("DELETE FROM SellEntity WHERE id = :id")
    suspend fun deleteSellById(id:Int)

    @Query("DELETE FROM SellProductEntity WHERE sellId = :sellId")
    suspend fun deleteProductFromSell(sellId:Int)

    @Transaction
    suspend fun deleteSellWithProducts(sellId:Int) {
        deleteProductFromSell(sellId)
        deleteSellById(sellId)
    }

}