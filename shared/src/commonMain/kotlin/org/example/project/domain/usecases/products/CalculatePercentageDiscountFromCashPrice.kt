package org.example.project.domain.usecases.products

class CalculatePercentageDiscountFromCashPrice {

    operator fun invoke(listPrice:Long, cashPrice:Long):Long {

        if(listPrice == 0L) return 0L
        if(cashPrice == 0L) return 0L

        return (((listPrice - cashPrice).toFloat() / listPrice) * 100).toLong()

    }

}