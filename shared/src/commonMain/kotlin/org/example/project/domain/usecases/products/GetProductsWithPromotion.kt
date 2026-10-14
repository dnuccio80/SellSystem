package org.example.project.domain.usecases.products

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductWithPromotion
import org.example.project.domain.repositories.ProductRepository
import org.example.project.domain.repositories.PromotionRepository
import org.example.project.domain.usecases.promotions.GetPromotionsForProduct

class GetProductsWithPromotion(
    private val productRepo: ProductRepository,
    private val promotionRepo: PromotionRepository,
    private val getPromotionsForProduct: GetPromotionsForProduct,
) {

    operator fun invoke(query: String): Flow<List<ProductWithPromotion>> {

        val productList = if(query.isBlank()) productRepo.getAllProducts() else productRepo.getProductsByQuery(query)

        return combine(
            productList,
            promotionRepo.getAllPromotions()
        ) { products, promotions ->
            products.map { product ->
                ProductWithPromotion(
                    product = product,
                    promotion = getPromotionsForProduct(product, promotions)
                )
            }.sortedByDescending { !it.product.manageStock || it.product.currentStock > 0  }
        }

    }

}