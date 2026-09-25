package org.example.project.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.sell.ProductWithQuantity
import org.example.project.domain.models.sell.Sell

interface SellRepository {
    fun getAllSells(): Flow<List<Sell>>
    fun getSellsByQuery(query:String): Flow<List<Sell>>
    suspend fun getSellById(id:Int): Sell
    suspend fun addSellWithProducts(sell: Sell, products:List<ProductWithQuantity>)
    suspend fun deleteSellById(sellId:Int)
    suspend fun getProductsFromSell(sellId:Int):List<ProductWithQuantity>
}