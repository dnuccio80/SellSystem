package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.data.db.entities.relations.SellProductEntity
import org.example.project.domain.models.product.ProductWithPromotion
import org.example.project.domain.models.promotions.Promotion
import org.example.project.domain.models.sell.ProductWithQuantity
import org.example.project.domain.models.sell.Sell
import org.example.project.domain.repositories.SellRepository

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

    override suspend fun addSellWithProducts(
        sell: Sell,
        products: List<ProductWithQuantity>,
    ) {
        db.sellDao().addSellWithProducts(
            sell = sell.toEntity(),
            products = products.map {
                SellProductEntity(
                    sellId = 0,
                    productId = it.productWithPromo.product.id,
                    promoId = it.productWithPromo.promotion?.id,
                    quantity = it.quantity
                )
            }
        )
    }

    override suspend fun deleteSellById(sellId: Int) {
        db.sellDao().deleteSellWithProducts(sellId)
    }

    override suspend fun getProductsFromSell(sellId: Int): List<ProductWithQuantity> {
        return db.sellDao().getProductsFromSell(sellId).map { sellProductEntity ->
            val product =
                db.productDao().getProductById(id = sellProductEntity.productId).toDomain()
            val promo: Promotion =
                db.promotionDao().getPromotionById(sellProductEntity.sellId).toDomain()

            ProductWithQuantity(
                productWithPromo = ProductWithPromotion(
                    product,
                    promo
                ),
                quantity = sellProductEntity.quantity,
            )
        }
    }
}