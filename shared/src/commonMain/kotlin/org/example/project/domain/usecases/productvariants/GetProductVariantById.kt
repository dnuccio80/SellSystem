package org.example.project.domain.usecases.productvariants

import org.example.project.data.db.repositoriesimpl.ProductVariantRepositoryImpl
import org.example.project.domain.models.product.ProductVariant

class GetProductVariantById(private val repo: ProductVariantRepositoryImpl) {

    suspend operator fun invoke(id:Int): ProductVariant = repo.getProductVariantById(id)

}