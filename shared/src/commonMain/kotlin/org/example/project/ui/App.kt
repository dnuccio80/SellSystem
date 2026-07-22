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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CreditScore
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.CrossfadeTransition
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.SlideTransition
import org.example.project.ui.screens.ClientsListScreen
import org.example.project.ui.screens.CurrentAccountsListScreen
import org.example.project.ui.screens.DailyScreen
import org.example.project.ui.screens.DashboardScreen
import org.example.project.ui.screens.NewSellScreen
import org.example.project.ui.screens.ProductsScreen
import org.example.project.ui.screens.SellsListScreen
import org.example.project.ui.screens.SuppliersListScreen
import org.example.project.ui.utils.CardTitleBackground
import org.example.project.ui.utils.FullCard
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.WhiteText

@Composable
@Preview
fun App() {
    MaterialTheme {

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
                                    SideBar(menuItemList, menuItemSelected) {newMenuItemSelected ->
                                        menuItemSelected = newMenuItemSelected
                                        when(menuItemSelected) {
                                            Routes.Dashboard.route -> if(currentScreen !is DashboardScreen) {
                                                navigator.popUntilRoot()
                                                navigator.replace(DashboardScreen())
                                            }
                                            Routes.Daily.route -> if(currentScreen !is DailyScreen) {
                                                navigator.popUntilRoot()
                                                navigator.replace(DailyScreen())
                                            }
                                            Routes.Products.route -> if(currentScreen !is ProductsScreen) {
                                                navigator.popUntilRoot()
                                                navigator.replace(ProductsScreen())
                                            }
                                            Routes.Clients.route -> if(currentScreen !is ClientsListScreen) {
                                                navigator.popUntilRoot()
                                                navigator.replace(ClientsListScreen())
                                            }
                                            Routes.CurrentAccounts.route -> if(currentScreen !is CurrentAccountsListScreen) {
                                                navigator.popUntilRoot()
                                                navigator.replace(CurrentAccountsListScreen())
                                            }
                                            Routes.Suppliers.route -> if(currentScreen !is SuppliersListScreen) {
                                                navigator.popUntilRoot()
                                                navigator.replace(SuppliersListScreen())
                                            }
                                            Routes.Sells.route -> if(currentScreen !is SellsListScreen) {
                                                navigator.popUntilRoot()
                                                navigator.replace(SellsListScreen())
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
                ) {newMenuItem ->
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

@Composable
fun ContentContainer() {
    ScreenContainer {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, GrayText),
                ) {
                    Text("Boton prueba", style = MaterialTheme.typography.bodyLarge)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, GrayText),
                ) {
                    Text("Boton prueba", style = MaterialTheme.typography.bodyLarge)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, GrayText),
                ) {
                    Text("Boton prueba", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        Card(shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth()) {
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = CardTitleBackground)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = { },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = GrayText,
                                checkedColor = GreenText
                            )
                        )
                        Text(
                            "SKU",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Imagen",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Titulo",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Categoria",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Cantidad",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Precio",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Ult. modif.",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Estado",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Outlined.FilterAlt,
                                contentDescription = "filter button",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
