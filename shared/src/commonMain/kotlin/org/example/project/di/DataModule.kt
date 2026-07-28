package org.example.project.di

import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductVariantRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::ClientRepositoryImpl)
    singleOf(::ProductVariantRepositoryImpl)
    singleOf(::ProductRepositoryImpl)
}