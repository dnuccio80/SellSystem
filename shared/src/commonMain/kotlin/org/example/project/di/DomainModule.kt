package org.example.project.di

import org.example.project.domain.repositories.ClientRepository
import org.example.project.domain.usecases.clients.AddNewClient
import org.example.project.domain.usecases.clients.GetClientById
import org.example.project.domain.usecases.clients.GetClients
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
//    Clients
    factoryOf(::GetClients)
    factoryOf(::AddNewClient)
    factoryOf(::GetClientById)
}