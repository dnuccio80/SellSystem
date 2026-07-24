package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.ProductVariant
import org.example.project.domain.repositories.ProductVariantRepository

class ProductVariantImpl(private val db: SystemDatabase): ProductVariantRepository {
    override fun getAllProductVariants(): Flow<List<ProductVariant>> {
        return db.productVariantDao().getAllProductVariants().map { list ->
            list.map { productVariant ->
                productVariant.toDomain()
            }
        }
    }

    override suspend fun addProductVariant(productVariant: ProductVariant) {
        db.productVariantDao().addProductVariant(productVariant.toEntity())
    }

    override suspend fun deleteProductVariantById(id: Int) {
        db.productVariantDao().deleteProductVariantById(id)
    }
}