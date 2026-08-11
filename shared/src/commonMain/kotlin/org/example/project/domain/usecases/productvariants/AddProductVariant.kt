package org.example.project.domain.usecases.productvariants

import org.example.project.domain.models.product.ProductVariant
import org.example.project.domain.repositories.ProductVariantRepository

class AddProductVariant(private val productVariantRepo: ProductVariantRepository) {

    suspend operator fun invoke(productVariant: ProductVariant) {

        val filteredVariantList = productVariant.variants.filter { it.isNotBlank() }

        productVariantRepo.addProductVariant(productVariant.copy(variants = filteredVariantList))
    }

}