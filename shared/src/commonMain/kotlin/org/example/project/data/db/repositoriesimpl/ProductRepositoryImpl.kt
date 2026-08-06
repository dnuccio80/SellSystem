package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.product.Product
import org.example.project.domain.repositories.ProductRepository

class ProductRepositoryImpl(private val db: SystemDatabase): ProductRepository {
    override fun getAllProducts(): Flow<List<Product>> {
        return db.productDao().getAllProducts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getProductsByQuery(query: String): Flow<List<Product>> {
        return db.productDao().getProductBySearch(query).map { list ->
            list.map { it.toDomain() }
        }
    }


    override suspend fun getProductById(id: Int): Product {
        return db.productDao().getProductById(id).toDomain()
    }

    override suspend fun addProduct(product: Product) {
        db.productDao().addProduct(product.toEntity())
    }

    override suspend fun deleteProductById(id: Int) {
        db.productDao().deleteProductById(id)
    }
}