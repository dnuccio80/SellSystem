package org.example.project.domain.usecases.products

import org.example.project.ui.ext.toPercentAdd

class CalculatePercentageProfitFromSellPrice {

    operator fun invoke(buyPrice: Long, sellPrice: Long): Long {

        if(sellPrice == 0L) return 0L
        if(buyPrice == 0L) return 0L


        val percent = (sellPrice - buyPrice).toFloat() / sellPrice * 100

        return percent.toLong()
    }

}