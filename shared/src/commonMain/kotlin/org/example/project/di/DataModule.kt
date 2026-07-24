package org.example.project.di

import org.example.project.data.db.daos.ClientDao
import org.example.project.data.db.repositoriesimpl.ClientRepositoryImpl
import org.example.project.data.db.repositoriesimpl.ProductVariantImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::ClientRepositoryImpl)
    singleOf(::ProductVariantImpl)
}