package org.example.project.domain.models.daily

import kotlinx.datetime.LocalDate

data class DailyData(
    val initialCashAmount:Long,
    val initialVirtualAccountAmount:Long,
    val finishCashAmount:Long,
    val finishVirtualAccountAmount:Long,
    val currentAccountAmountAdded:Long,
    val date: LocalDate? = null
)

class CleanDailyData {
    fun getCleanDailyData(): DailyData {
        return DailyData(
            initialCashAmount = 0L,
            initialVirtualAccountAmount = 0L,
            finishCashAmount = 0L,
            finishVirtualAccountAmount = 0L,
            currentAccountAmountAdded = 0L,
        )
    }
}
