package org.example.project.domain.usecases.currentaccounts

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import org.example.project.domain.models.currentaccount.ClientWithCurrentAccount
import org.example.project.domain.models.currentaccount.CurrentAccountFinancialReport
import org.example.project.domain.models.currentaccount.CurrentAccountSummary
import org.example.project.domain.repositories.CurrentAccountRepository

class GetCurrentAccountFinancialReport {

    operator fun invoke(list:List<ClientWithCurrentAccount>): CurrentAccountFinancialReport {


        if(list.isEmpty()) return CurrentAccountFinancialReport(
            totalAccounts = 0,
            totalAmountInAllAccounts = 0,
            highestCurrentAccount = CurrentAccountSummary()
        )

        val totalCurrentAccount = list.size
        val totalBalance = list.sumOf { it.currentAccount!!.amount  }
        val highestCurrentAccount = list.maxBy { it.currentAccount!!.amount }

        return CurrentAccountFinancialReport(
            totalAccounts = totalCurrentAccount,
            totalAmountInAllAccounts = totalBalance,
            highestCurrentAccount = CurrentAccountSummary(
                name = highestCurrentAccount.client.fullName.ifBlank { "Sin existencia" },
                amount = highestCurrentAccount.currentAccount?.amount ?: 0
            )
        )
    }

}