package org.example.project.domain.usecases.products

import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.domain.models.Product

class AddProduct(private val repo: ProductRepositoryImpl) {

    suspend operator fun invoke(product: Product) {

        repo.addProduct(product)

    }

}