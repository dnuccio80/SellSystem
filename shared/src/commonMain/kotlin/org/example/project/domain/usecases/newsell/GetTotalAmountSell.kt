package org.example.project.domain.usecases.newsell

import org.example.project.domain.models.promotions.PromotionCategory
import org.example.project.domain.models.promotions.PromotionType
import org.example.project.domain.models.sell.ProductWithQuantity
import org.example.project.ui.models.ProductWithQuantityPresentation

enum class PaymentMethod(val etiquette:String) {
    CASH("Efectivo"), TRANSFER("Transferencia"), CARD("Tarjeta"), CURRENT_ACCOUNT("Cuenta corriente")
}

data class SellAmounts(
    val subtotal: Long,
    val paymentDiscounts: Long,
    val promoDiscounts:Long,
    val total: Long,
)
class GetTotalAmountSell {
    operator fun invoke(
        paymentMethod: PaymentMethod,
        productWithQuantityList: List<ProductWithQuantityPresentation>,
    ): SellAmounts {

        return when (paymentMethod) {
            PaymentMethod.CASH, PaymentMethod.TRANSFER -> {

                val subTotal = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.productWithPromo.product.listPrice * productWithQuantity.quantity
                }
                val cashPrice = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.productWithPromo.product.cashPrice * productWithQuantity.quantity
                }

                val promoDiscount = productWithQuantityList.sumOf { productWithQuantity ->
                    getPromoDiscount(productWithQuantity)
                }

                SellAmounts(
                    subtotal = subTotal,
                    paymentDiscounts = subTotal - cashPrice,
                    promoDiscounts = promoDiscount,
                    total = cashPrice
                )

            }
            PaymentMethod.CURRENT_ACCOUNT,PaymentMethod.CARD  -> {
                val subTotal = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.productWithPromo.product.listPrice * productWithQuantity.quantity
                }

                SellAmounts(
                    subtotal = subTotal,
                    promoDiscounts = 0L,
                    paymentDiscounts = 0,
                    total = subTotal
                )
            }
        }
    }
}

private fun getPromoDiscount(productWithQuantity: ProductWithQuantityPresentation):Long {

    return when(productWithQuantity.productWithPromo.promotion?.promotionType) {
        PromotionType.BUY_X_PAY_Y -> {

            val buyQuantity = productWithQuantity.productWithPromo.promotion.buyX ?: 0
            val payQuantity = productWithQuantity.productWithPromo.promotion.payY ?: 0
            val price = productWithQuantity.productWithPromo.product.listPrice
            val quantity = productWithQuantity.quantity

            if (buyQuantity <= 0 || payQuantity !in 0..buyQuantity) return 0L
            val freeQuantity = (quantity / buyQuantity) * (buyQuantity - payQuantity)

            freeQuantity * price

        }
        PromotionType.PERCENT -> {
            val unitPrice = productWithQuantity.productWithPromo.product.listPrice
            val discountedAmount = (unitPrice * (productWithQuantity.productWithPromo.promotion.percent!!.toFloat() / 100)).toLong()
            val quantity = productWithQuantity.quantity

            discountedAmount * quantity
        }
        else -> { 0L }
    }
}