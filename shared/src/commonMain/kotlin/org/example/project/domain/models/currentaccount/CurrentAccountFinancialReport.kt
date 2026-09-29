package org.example.project.domain.models.currentaccount

data class CurrentAccountFinancialReport(
    val totalAccounts:Int = 0,
    val totalAmountInAllAccounts:Long = 0L,
    val highestCurrentAccount:CurrentAccountSummary
)

data class CurrentAccountSummary(
    val name:String = "",
    val amount:Long = 0,
)
