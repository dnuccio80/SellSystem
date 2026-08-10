package org.example.project.di

import org.example.project.ui.screens.addproducts.AddProductViewModel
import org.example.project.ui.screens.clients.ClientsViewModel
import org.example.project.ui.screens.expenses.ExpensesViewModel
import org.example.project.ui.screens.products.ProductsViewModel
import org.example.project.ui.screens.productvariants.ProductVariantsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::ClientsViewModel)
    viewModelOf(::ProductVariantsViewModel)
    viewModelOf(::ProductsViewModel)
    viewModelOf(::AddProductViewModel)
    viewModelOf(::ExpensesViewModel)
}