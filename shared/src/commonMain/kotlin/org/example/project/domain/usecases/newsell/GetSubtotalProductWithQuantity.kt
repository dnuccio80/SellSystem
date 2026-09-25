package org.example.project.domain.usecases.newsell

import org.example.project.domain.models.product.Product

class GetSubtotalProductWithQuantity {
    operator fun invoke(product: Product, quantity: Int):Long {
        return product.listPrice * quantity
    }
}