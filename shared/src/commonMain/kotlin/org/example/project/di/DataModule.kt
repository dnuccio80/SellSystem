package org.example.project.di

import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.data.db.repositoriesimpl.CurrentAccountRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ExpenseRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductCategoryRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductVariantRepositoryImpl
import org.example.project.data.db.repositoriesimpl.SupplierRepositoryImpl
import org.example.project.domain.repositories.ClientRepository
import org.example.project.domain.repositories.CurrentAccountRepository
import org.example.project.domain.repositories.ExpensesRepository
import org.example.project.domain.repositories.ProductCategoryRepository
import org.example.project.domain.repositories.ProductRepository
import org.example.project.domain.repositories.ProductVariantRepository
import org.example.project.domain.repositories.SupplierRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single<ProductVariantRepository> { ProductVariantRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<ClientRepository> { ClientRepositoryImpl(get()) }
    single<ProductCategoryRepository> { ProductCategoryRepositoryImpl(get()) }
    single<ExpensesRepository> { ExpenseRepositoryImpl(get()) }
    single<SupplierRepository> { SupplierRepositoryImpl(get()) }
    single<CurrentAccountRepository> { CurrentAccountRepositoryImpl(get()) }
}