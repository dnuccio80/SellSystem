package org.example.project.di

import org.example.project.domain.repositories.ClientRepository
import org.example.project.domain.usecases.clients.AddNewClient
import org.example.project.domain.usecases.clients.GetClientById
import org.example.project.domain.usecases.clients.GetClients
import org.example.project.domain.usecases.products.AddProduct
import org.example.project.domain.usecases.products.GetProducts
import org.example.project.domain.usecases.products.ModifyProduct
import org.example.project.domain.usecases.productvariants.AddProductVariant
import org.example.project.domain.usecases.productvariants.GetProductVariantById
import org.example.project.domain.usecases.productvariants.GetProductVariants
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
//    Clients
    factoryOf(::GetClients)
    factoryOf(::AddNewClient)
    factoryOf(::GetClientById)
//    ProductVariant
    factoryOf(::AddProductVariant)
    factoryOf(::GetProductVariants)
    factoryOf(::GetProductVariantById)
//    Products
    factoryOf(::GetProducts)
    factoryOf(::AddProduct)
    factoryOf(::ModifyProduct)
}