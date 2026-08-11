package org.example.project.domain.usecases.productvariants

import org.example.project.domain.models.product.ProductVariant
import org.example.project.domain.repositories.ProductVariantRepository

class GetProductVariantById(private val repo: ProductVariantRepository) {

    suspend operator fun invoke(id:Int): ProductVariant = repo.getProductVariantById(id)

}