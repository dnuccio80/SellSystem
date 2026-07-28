package org.example.project.domain.usecases.products

import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.domain.models.Product

class GetProducts(private val repo: ProductRepositoryImpl) {

    operator fun invoke(query:String): Flow<List<Product>> {
        return if(query.isBlank()) repo.getAllProducts()
        else repo.getProductsByQuery(query)
    }

}