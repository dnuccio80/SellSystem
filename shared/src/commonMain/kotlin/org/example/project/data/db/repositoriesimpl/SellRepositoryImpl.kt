package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.data.db.daos.SellRepository
import org.example.project.domain.models.sell.Sell

class SellRepositoryImpl(private val db: SystemDatabase) : SellRepository {
    override fun getAllSells(): Flow<List<Sell>> {
        return db.sellDao().getAllSells().map { list ->
            list.map { it.toDomain() }.reversed()
        }
    }

    override fun getSellsByQuery(query: String): Flow<List<Sell>> {
        return db.sellDao().getSellsByQuery(query).map { list ->
            list.map { it.toDomain() }.reversed()
        }
    }

    override suspend fun getSellById(id: Int): Sell {
        return db.sellDao().getSellById(id).toDomain()
    }

    override suspend fun addSell(sell: Sell) {
        db.sellDao().addSell(sell.toEntity())
    }

    override suspend fun deleteSellById(id: Int) {
        db.sellDao().deleteSellById(id)
    }
}