package org.example.project.domain.usecases.products

import org.example.project.data.db.repositoriesimpl.ProductRepositoryImpl
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductError

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
            product.manageExpireDate && product.expireDate == null -> throw ProductError.NotExpireDateSelected
        }

        val managedStockProduct = if(!product.manageStock) product.copy(currentStock = 0, adviceStock = 0) else product
        val managedExpireDateProduct = if(!managedStockProduct.manageExpireDate) managedStockProduct.copy(expireDate = null) else product

        repo.addProduct(managedExpireDateProduct)

    }

}

