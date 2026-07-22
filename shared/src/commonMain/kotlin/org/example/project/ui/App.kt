package org.example.project.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CreditScore
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.example.project.ui.di.uiModule
import org.example.project.ui.screens.ClientsListScreen
import org.example.project.ui.screens.CurrentAccountsListScreen
import org.example.project.ui.screens.DailyScreen
import org.example.project.ui.screens.DashboardScreen
import org.example.project.ui.screens.ExpensesScreen
import org.example.project.ui.screens.FinancialReportsScreen
import org.example.project.ui.screens.LoyaltySystemScreen
import org.example.project.ui.screens.PendingOrdersScreen
import org.example.project.ui.screens.ProductsScreen
import org.example.project.ui.screens.PromotionsScreen
import org.example.project.ui.screens.ReturnsScreen
import org.example.project.ui.screens.SellsListScreen
import org.example.project.ui.screens.SuppliersListScreen
import org.example.project.ui.utils.CardTitleBackground
import org.example.project.ui.utils.FullCard
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.koin.compose.KoinApplication

@Composable
fun App() {
    MaterialTheme {
        KoinApplication(application = { modules(uiModule) }) {
            val menuItemList = listOf(
                MenuItemData(
                    title = "Panel principal",
                    route = Routes.Dashboard,
                    icon = Icons.Default.Dashboard
                ),
                MenuItemData(
                    title = "Caja diaria",
                    route = Routes.Daily,
                    icon = Icons.Default.AttachMoney
                ),
                MenuItemData(
                    title = "Productos",
                    route = Routes.Products,
                    icon = Icons.Outlined.LocalGroceryStore
                ),
                MenuItemData(
                    title = "Clientes",
                    route = Routes.Clients,
                    icon = Icons.Outlined.Person
                ),
                MenuItemData(
                    title = "Cuentas corrientes",
                    route = Routes.CurrentAccounts,
                    icon = Icons.Outlined.CreditScore
                ),
                MenuItemData(
                    title = "Proveedores",
                    route = Routes.Suppliers,
                    icon = Icons.Outlined.LocalShipping
                ),
                MenuItemData(
                    title = "Ventas",
                    route = Routes.Sells,
                    icon = Icons.Outlined.Sell
                ),
                MenuItemData(
                    title = "Órdenes pendientes",
                    route = Routes.PendingOrders,
                    icon = Icons.AutoMirrored.Outlined.ListAlt
                ),
                MenuItemData(
                    title = "Gastos",
                    route = Routes.Expenses,
                    icon = Icons.Outlined.Payments
                ),
                MenuItemData(
                    title = "Reportes financieros",
                    route = Routes.FinancialReports,
                    icon = Icons.Outlined.BarChart
                ),
                MenuItemData(
                    title = "Sistema de lealtad",
                    route = Routes.LoyaltySystem,
                    icon = Icons.Outlined.Star
                ),
                MenuItemData(
                    title = "Promociones",
                    route = Routes.Promotions,
                    icon = Icons.Outlined.Discount
                ),
                MenuItemData(
                    title = "Devoluciones",
                    route = Routes.Returns,
                    icon = Icons.Outlined.Replay
                ),
            )

            var menuItemSelected by remember { mutableStateOf(Routes.Dashboard.route) }

            Navigator(screen = DashboardScreen()) { navigator ->

                val currentScreen = navigator.lastItem

                Box(modifier = Modifier.fillMaxSize().background(FullCard)) {
                    Column(
                        modifier = Modifier
                            .safeContentPadding()
                            .fillMaxSize(),
                    ) {
                        Scaffold(containerColor = FullCard) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Column {
                                    MainHeader()
                                    Row {
                                        SideBar(
                                            menuItemList,
                                            menuItemSelected
                                        ) { newMenuItemSelected ->
                                            menuItemSelected = newMenuItemSelected
                                            when (menuItemSelected) {
                                                Routes.Dashboard.route -> if (currentScreen !is DashboardScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(DashboardScreen())
                                                }

                                                Routes.Daily.route -> if (currentScreen !is DailyScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(DailyScreen())
                                                }

                                                Routes.Products.route -> if (currentScreen !is ProductsScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(ProductsScreen())
                                                }

                                                Routes.Clients.route -> if (currentScreen !is ClientsListScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(ClientsListScreen())
                                                }

                                                Routes.CurrentAccounts.route -> if (currentScreen !is CurrentAccountsListScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(CurrentAccountsListScreen())
                                                }

                                                Routes.Suppliers.route -> if (currentScreen !is SuppliersListScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(SuppliersListScreen())
                                                }

                                                Routes.Sells.route -> if (currentScreen !is SellsListScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(SellsListScreen())
                                                }

                                                Routes.PendingOrders.route -> if (currentScreen !is PendingOrdersScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(PendingOrdersScreen())
                                                }

                                                Routes.Expenses.route -> if (currentScreen !is ExpensesScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(ExpensesScreen())
                                                }

                                                Routes.FinancialReports.route -> if (currentScreen !is FinancialReportsScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(FinancialReportsScreen())
                                                }

                                                Routes.LoyaltySystem.route -> if (currentScreen !is LoyaltySystemScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(LoyaltySystemScreen())
                                                }

                                                Routes.Promotions.route -> if (currentScreen !is PromotionsScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(PromotionsScreen())
                                                }

                                                Routes.Returns.route -> if (currentScreen !is ReturnsScreen) {
                                                    navigator.popUntilRoot()
                                                    navigator.replace(ReturnsScreen())
                                                }
                                            }
                                        }
                                        FadeTransition(navigator)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SideBar(menuItemList: List<MenuItemData>, menuItemSelected: String, onClick: (String) -> Unit) {
    Box(modifier = Modifier.background(FullCard)) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            menuItemList.forEach { item ->
                MenuItem(
                    item, menuItemSelected,
                ) { newMenuItem ->
                    onClick(newMenuItem)
                }
            }
        }
    }
}

@Composable
private fun MenuItem(
    itemData: MenuItemData,
    menuItemSelected: String,
    onClick: (String) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier =
            Modifier
                .background(
                    if (menuItemSelected == itemData.route.route) GreenText.copy(
                        alpha = .3f
                    ) else if (isHovered) PrimaryCardBackground else Color.Transparent
                )
                .hoverable(interactionSource)
                .clickable { onClick(itemData.route.route) }
                .pointerHoverIcon(PointerIcon.Hand)

    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(itemData.icon, contentDescription = "menu item", tint = Color.White)
            Text(itemData.title, color = Color.White, fontSize = 14.sp)
        }
    }
}

