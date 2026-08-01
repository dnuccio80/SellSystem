package org.example.project.domain.usecases.products

import org.example.project.ui.ext.toPrice

class CalculatePriceFromPercentage {
    operator fun invoke(buyPrice: Long, percentage: Long): Long {

        if(buyPrice == 0L) return 0L
        if(percentage == 0L) return 0L

        val percentageInFloat = percentage.toFloat()/100

        return (buyPrice/(1-percentageInFloat)).toLong()
    }
}