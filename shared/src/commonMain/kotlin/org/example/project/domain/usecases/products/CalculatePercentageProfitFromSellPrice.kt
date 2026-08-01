package org.example.project.domain.usecases.products

import org.example.project.ui.ext.toPercentAdd

class CalculatePercentageProfitFromSellPrice {

    operator fun invoke(buyPrice: Long, sellPrice: Long): String {

        if(sellPrice == 0L) return "0%"
        if(buyPrice == 0L) return "0%"


        val percent = (sellPrice - buyPrice).toFloat() / sellPrice * 100

        if(percent < 0) return "Margen negativo"

        return percent.toLong().toPercentAdd()
    }

}