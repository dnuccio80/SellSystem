package org.example.project.domain.usecases.products

import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.domain.models.Product
import org.example.project.domain.models.ProductError

class AddProduct(private val repo: ProductRepositoryImpl) {

    suspend operator fun invoke(product: Product) {

        when {
            product.listPrice < product.buyPrice -> throw ProductError.ListPriceLessThanBuyPrice
            product.cashPrice > product.listPrice -> throw ProductError.CashPriceMoreThenListPrice
            product.cashPrice < product.buyPrice -> throw ProductError.CashPriceLessThanBuyPrice
            product.listPrice <= 0 -> throw ProductError.NoListPriceData
            product.cashPrice <= 0 -> throw ProductError.NoCashPriceData
            product.buyPrice <= 0 -> throw ProductError.NoBuyPriceData
            product.name.isBlank() || product.brand.isBlank() -> throw ProductError.NotEnoughData
            product.manageStock && product.currentStock == 0 -> throw ProductError.InvalidStockData
        }

        val managedStockProduct = if(!product.manageStock) product.copy(currentStock = 0, adviceStock = 0) else product

        repo.addProduct(managedStockProduct)

    }

}

