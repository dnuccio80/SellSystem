package org.example.project.domain.usecases.sells

import org.example.project.domain.models.sell.Sell
import org.example.project.domain.models.sell.SellFinancialReport

class GetFinancialSellReport {

    operator fun invoke(sellList:List<Sell>): SellFinancialReport {

        val totalEarning = sellList.sumOf { it.total }
        val totalSells = sellList.size
        val sellsToClients = sellList.filter { it.clientName.isNotBlank() }.size
        val sellToGenericClient = totalSells - sellsToClients

        return SellFinancialReport(
            totalEarnings = totalEarning,
            totalSells = totalSells,
            totalSellsToClients = sellsToClients,
            totalSellsToGenericClients = sellToGenericClient
        )
    }

}