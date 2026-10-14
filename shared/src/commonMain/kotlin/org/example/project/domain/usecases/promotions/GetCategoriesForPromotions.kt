package org.example.project.domain.usecases.promotions

import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.repositories.ProductCategoryRepository

class GetCategoriesForPromotions(private val categoryRepository: ProductCategoryRepository) {

    suspend operator fun invoke(categoryIds:List<Int>):List<ProductCategory> {
        if(categoryIds.isEmpty()) return emptyList()

        val categoryList = mutableListOf<ProductCategory>()

        categoryIds.forEach { id ->
            categoryList.add(categoryRepository.getCategoryById(id))
        }

        return categoryList
    }

}