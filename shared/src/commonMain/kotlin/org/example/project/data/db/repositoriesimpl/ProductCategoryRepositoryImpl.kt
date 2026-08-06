package org.example.project.data.db.repositoriesimpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.db.SystemDatabase
import org.example.project.domain.models.ProductCategory
import org.example.project.domain.repositories.ProductCategoryRepository

class ProductCategoryRepositoryImpl(val db: SystemDatabase): ProductCategoryRepository {
    override fun getAllCategories(): Flow<List<ProductCategory>> {
        return db.productCategoryDao().getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getCategoryById(id: Int): ProductCategory {
        return db.productCategoryDao().getCategoryById(id).toDomain()
    }

    override fun getCategoriesByQuery(query: String): Flow<List<ProductCategory>> {
        return db.productCategoryDao().getCategoriesByQuery(query).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addCategory(productCategory: ProductCategory) {
        db.productCategoryDao().addCategory(productCategory.toEntity())
    }

    override suspend fun deleteCategory(id: Int) {
        db.productCategoryDao().deleteCategory(id)
    }
}