package org.example.project.domain.usecases.promotions

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.filter
import org.example.project.domain.models.product.Product
import org.example.project.domain.repositories.ProductRepository

class GetProductsForPromotions(private val productRepository: ProductRepository) {

    suspend operator fun invoke(productIds:List<Int>):List<Product> {

        if(productIds.isEmpty()) return emptyList()

        val productsList = mutableListOf<Product>()

        productIds.forEach { id ->
            productsList.add(productRepository.getProductById(id))
        }

        return productsList

    }

}