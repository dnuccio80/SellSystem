package org.example.project.ui

sealed class Routes(val route:String) {
    data object Dashboard: Routes("dashboard")
    data object Daily: Routes("daily")
    data object Products: Routes("products")
    data object Clients: Routes("clients")
    data object CurrentAccounts: Routes("currentAccounts")
    data object Suppliers: Routes("suppliers")
    data object Sells: Routes("sells")
    data object PendingOrders: Routes("pendingOrders")
    data object Expenses: Routes("expenses")
    data object FinancialReports: Routes("financialReports")
    data object LoyaltySystem: Routes("loyaltySystem")
    data object Promotions: Routes("promotions")
    data object Returns: Routes("returns")
    data object ProductVariants: Routes("productVariants")
}
