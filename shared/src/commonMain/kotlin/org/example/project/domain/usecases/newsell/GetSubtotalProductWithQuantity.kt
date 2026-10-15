package org.example.project.domain.usecases.newsell

import org.example.project.domain.models.product.ProductWithPromo
import org.example.project.domain.models.product.ProductWithPromotion

class GetSubtotalProductWithQuantity {
    operator fun invoke(productWithPromo: ProductWithPromotion, quantity: Int):Long {
        return productWithPromo.product.listPrice * quantity
    }
}