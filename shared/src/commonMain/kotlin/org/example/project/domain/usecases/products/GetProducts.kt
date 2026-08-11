package org.example.project.domain.usecases.products

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.product.Product
import org.example.project.domain.repositories.ProductRepository

class GetProducts(private val repo: ProductRepository) {

    operator fun invoke(query:String): Flow<List<Product>> {
        return if(query.isBlank()) repo.getAllProducts()
        else repo.getProductsByQuery(query)
    }

}