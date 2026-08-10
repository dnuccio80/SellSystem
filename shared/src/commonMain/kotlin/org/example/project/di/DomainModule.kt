package org.example.project.di

import org.example.project.domain.models.expense.AddExpense
import org.example.project.domain.usecases.clients.AddNewClient
import org.example.project.domain.usecases.clients.GetClientById
import org.example.project.domain.usecases.clients.GetClients
import org.example.project.domain.usecases.expenses.GetComposedExpensesFinance
import org.example.project.domain.usecases.expenses.GetExpenses
import org.example.project.domain.usecases.expenses.GetSingleExpenseFinance
import org.example.project.domain.usecases.products.AddCategory
import org.example.project.domain.usecases.products.AddProduct
import org.example.project.domain.usecases.products.CalculatePercentageDiscountFromCashPrice
import org.example.project.domain.usecases.products.CalculatePercentageProfitFromSellPrice
import org.example.project.domain.usecases.products.CalculatePriceFromPercentageAdd
import org.example.project.domain.usecases.products.CalculatePriceFromPercentageDiscount
import org.example.project.domain.usecases.products.GetCategories
import org.example.project.domain.usecases.products.GetProducts
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
    factoryOf(::CalculatePercentageProfitFromSellPrice)
    factoryOf(::CalculatePriceFromPercentageAdd)
    factoryOf(::CalculatePercentageDiscountFromCashPrice)
    factoryOf(::CalculatePriceFromPercentageDiscount)
    factoryOf(::GetCategories)
    factoryOf(::AddCategory)
//    Expenses
    factoryOf(::GetExpenses)
    factoryOf(::AddExpense)
    factoryOf(::GetComposedExpensesFinance)
    factoryOf(::GetSingleExpenseFinance)

}