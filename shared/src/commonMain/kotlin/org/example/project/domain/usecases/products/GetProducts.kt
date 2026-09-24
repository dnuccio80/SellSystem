package org.example.project.domain.usecases.products

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.groupBy
import kotlinx.coroutines.flow.map
import org.example.project.domain.models.product.Product
import org.example.project.domain.repositories.ProductRepository

class GetProducts(private val repo: ProductRepository) {

    operator fun invoke(query: String): Flow<List<Product>> {
        return if (query.isBlank()) repo.getAllProducts().map { list ->
            list.sortedByDescending {
                !it.manageStock || it.currentStock > 0
            }
        }
        else repo.getProductsByQuery(query).map { list ->
            list.sortedByDescending {
                !it.manageStock || it.currentStock > 0
            }
        }
    }

}