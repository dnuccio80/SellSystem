package org.example.project.di

import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ExpenseRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductCategoryRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductVariantRepositoryImpl
import org.example.project.data.db.repositoriesimpl.SupplierRepositoryImpl
import org.example.project.domain.repositories.ExpensesRepository
import org.example.project.domain.repositories.ProductCategoryRepository
import org.example.project.domain.repositories.SupplierRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::ClientRepositoryImpl)
    singleOf(::ProductVariantRepositoryImpl)
    singleOf(::ProductRepositoryImpl)
    single<ProductCategoryRepository> { ProductCategoryRepositoryImpl(get()) }
    single<ExpensesRepository> { ExpenseRepositoryImpl(get()) }
    single<SupplierRepository> { SupplierRepositoryImpl(get()) }
}