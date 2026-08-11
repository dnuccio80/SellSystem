package org.example.project.domain.usecases.productvariants

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.product.ProductVariant
import org.example.project.domain.repositories.ProductVariantRepository

class GetProductVariants(private val repo: ProductVariantRepository) {
    operator fun invoke(query: String): Flow<List<ProductVariant>> {
        return if (query.isBlank()) {
            repo.getAllProductVariants()
        } else {
            repo.getProductVariantsByName(query)
        }
    }
}