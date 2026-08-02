package org.example.project.domain.usecases.products

class CalculatePriceFromPercentageDiscount {

    operator fun invoke(listPrice:Long, discount:Long): Long {

        if(listPrice == 0L) return 0L
        if(discount == 0L) return listPrice

        return (listPrice - ((discount * listPrice).toFloat()/ 100)).toLong()
    }

}