package org.example.project.domain.usecases.products

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.example.project.domain.models.Product

class ModifyProduct {

    operator fun invoke(): Flow<Product> {

        val product = Product(
            name = "",
            category = "",
            brand = "",
            buyPrice = 0L,
            listPrice = 0L,
            cashPrice = 0L,
            manageStock = true,
            currentStock = 0,
            adviceStock = 0,
            description = ""
        )




        return flowOf(product)


    }

}