package org.example.project.domain.usecases.newsell

import org.example.project.domain.models.product.Product
import org.example.project.ui.models.ProductWithQuantity

class GetSubtotalProductWithQuantity {
    operator fun invoke(product: Product, quantity: Int):Long {
        return product.listPrice * quantity
    }
}