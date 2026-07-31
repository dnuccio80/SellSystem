package org.example.project.domain.usecases.products

import org.example.project.ui.ext.toPrice

class CalculatePriceFromPercentage {
    operator fun invoke(buyPrice: Long, percentage: Long): String {

        if(buyPrice == 0L) return "$0"
        if(percentage == 0L) return "$0"

        val percentageInFloat = percentage.toFloat()/100

        return (buyPrice/(1-percentageInFloat)).toLong().toPrice()
    }
}