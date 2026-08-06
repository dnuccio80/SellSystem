package org.example.project.domain.usecases.productvariants

import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.repositoriesimpl.ProductVariantRepositoryImpl
import org.example.project.domain.models.product.ProductVariant

class GetProductVariants(private val repo: ProductVariantRepositoryImpl) {
    operator fun invoke(query:String): Flow<List<ProductVariant>> {
        return if(query.isBlank()) {
            repo.getAllProductVariants()
        }else {
            repo.getProductVariantsByName(query)
        }
    }
}