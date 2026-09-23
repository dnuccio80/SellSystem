package org.example.project.domain.models.sell

data class SellFinancialReport(
    val totalEarnings:Long = 0L,
    val totalSells:Int = 0,
    val totalSellsToClients:Int = 0,
    val totalSellsToGenericClients:Int = 0
)