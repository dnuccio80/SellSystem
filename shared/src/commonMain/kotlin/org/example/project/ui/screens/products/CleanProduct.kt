package org.example.project.ui.screens.products

import org.example.project.domain.models.product.ProductWithPromo

class CleanProduct {
    fun getCleanProduct(): ProductWithPromo {
       return ProductWithPromo(
           id = 0,
           name = "",
           category = "",
           brand = "",
           buyPrice = 0L,
           listPrice = 0L,
           cashPrice = 0L,
           currentStock = 0,
           adviceStock = 0,
           description = "",
           manageStock = true
       )
    }
}