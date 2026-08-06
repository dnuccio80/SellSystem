package org.example.project.domain.usecases.products

import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.product.ProductError
import org.example.project.domain.repositories.ProductCategoryRepository

class AddCategory(private val repository: ProductCategoryRepository) {

    suspend operator fun invoke(productCategory: ProductCategory) {
        if(productCategory.name.isBlank()) throw ProductError.NotEnoughData
        else repository.addCategory(productCategory.copy(name = productCategory.name.trim()))
    }

}