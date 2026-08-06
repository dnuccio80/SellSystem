package org.example.project.domain.usecases.products

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.repositories.ProductCategoryRepository

class GetCategories(private val repository: ProductCategoryRepository) {

    operator fun invoke(query:String): Flow<List<ProductCategory>> {
        return if(query.isNotBlank()) { repository.getCategoriesByQuery(query) }
        else repository.getAllCategories()
    }

}