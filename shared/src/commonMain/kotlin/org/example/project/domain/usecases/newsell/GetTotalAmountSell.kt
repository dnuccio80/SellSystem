package org.example.project.domain.usecases.newsell

import org.example.project.ui.models.ProductWithQuantity

enum class PaymentMethod(val etiquette:String) {
    CASH("Efectivo"), TRANSFER("Transferencia"), CARD("Tarjeta"), CURRENT_ACCOUNT("Cuenta corriente")
}

data class SellAmounts(
    val subtotal: Long,
    val discounts: Long,
    val total: Long,
)
class GetTotalAmountSell {
    operator fun invoke(
        paymentMethod: PaymentMethod,
        productWithQuantityList: List<ProductWithQuantity>,
    ): SellAmounts {

        return when (paymentMethod) {
            PaymentMethod.CASH -> {

                val subTotal = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.product.listPrice * productWithQuantity.quantity
                }
                val cashPrice = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.product.cashPrice * productWithQuantity.quantity
                }

                SellAmounts(
                    subtotal = subTotal,
                    discounts = subTotal - cashPrice,
                    total = cashPrice
                )

            }

            PaymentMethod.TRANSFER -> {
                val subTotal = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.product.listPrice * productWithQuantity.quantity
                }
                val cashPrice = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.product.cashPrice * productWithQuantity.quantity
                }

                SellAmounts(
                    subtotal = subTotal,
                    discounts = subTotal - cashPrice,
                    total = cashPrice
                )
            }

            PaymentMethod.CURRENT_ACCOUNT -> {
                val subTotal = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.product.listPrice * productWithQuantity.quantity
                }

                SellAmounts(
                    subtotal = subTotal,
                    discounts = 0,
                    total = subTotal
                )
            }

            PaymentMethod.CARD -> {
                val subTotal = productWithQuantityList.sumOf { productWithQuantity ->
                    productWithQuantity.product.listPrice * productWithQuantity.quantity
                }

                SellAmounts(
                    subtotal = subTotal,
                    discounts = 0,
                    total = subTotal
                )
            }
        }
    }
}