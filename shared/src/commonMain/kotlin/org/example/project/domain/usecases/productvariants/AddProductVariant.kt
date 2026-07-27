package org.example.project.domain.usecases.productvariants

import org.example.project.data.db.repositoriesimpl.ProductVariantRepositoryImpl
import org.example.project.domain.models.ProductVariant

class AddProductVariant(private val productVariantRepo: ProductVariantRepositoryImpl) {

    suspend operator fun invoke(productVariant: ProductVariant) {

        val filteredVariantList = productVariant.variants.filter { it.isNotBlank() }

        productVariantRepo.addProductVariant(productVariant.copy(variants = filteredVariantList))
    }

}