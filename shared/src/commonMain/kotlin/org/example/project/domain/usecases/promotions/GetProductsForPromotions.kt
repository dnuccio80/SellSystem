package org.example.project.domain.usecases.promotions

import org.example.project.domain.models.product.ProductWithPromo
import org.example.project.domain.repositories.ProductRepository

class GetProductsForPromotions(private val productRepository: ProductRepository) {

    suspend operator fun invoke(productIds:List<Int>):List<ProductWithPromo> {

        if(productIds.isEmpty()) return emptyList()

        val productsList = mutableListOf<ProductWithPromo>()

        productIds.forEach { id ->
            productsList.add(productRepository.getProductById(id))
        }

        return productsList

    }

}