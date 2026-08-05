package org.example.project.ui

//@Composable
//fun Adasda() {
//    MaterialTheme {
//        KoinApplication(application = { modules(dataModule, domainModule, uiModule, platformModule()) }) {
//            val menuItemList = listOf(
//                MenuItemData(
//                    title = "Panel principal",
//                    route = Routes.Dashboard,
//                    icon = Icons.Default.Dashboard
//                ),
//                MenuItemData(
//                    title = "Caja diaria",
//                    route = Routes.Daily,
//                    icon = Icons.Default.AttachMoney
//                ),
//                MenuItemData(
//                    title = "Productos",
//                    route = Routes.Products,
//                    icon = Icons.Outlined.LocalGroceryStore
//                ),
//                MenuItemData(
//                    title = "Variantes de productos",
//                    route = Routes.ProductVariants,
//                    icon = Icons.Outlined.Category
//                ),
//                MenuItemData(
//                    title = "Clientes",
//                    route = Routes.Clients,
//                    icon = Icons.Outlined.Person
//                ),
//                MenuItemData(
//                    title = "Cuentas corrientes",
//                    route = Routes.CurrentAccounts,
//                    icon = Icons.Outlined.CreditScore
//                ),
//                MenuItemData(
//                    title = "Proveedores",
//                    route = Routes.Suppliers,
//                    icon = Icons.Outlined.LocalShipping
//                ),
//                MenuItemData(
//                    title = "Ventas",
//                    route = Routes.Sells,
//                    icon = Icons.Outlined.Sell
//                ),
//                MenuItemData(
//                    title = "Órdenes pendientes",
//                    route = Routes.PendingOrders,
//                    icon = Icons.AutoMirrored.Outlined.ListAlt
//                ),
//                MenuItemData(
//                    title = "Gastos",
//                    route = Routes.Expenses,
//                    icon = Icons.Outlined.Payments
//                ),
//                MenuItemData(
//                    title = "Reportes financieros",
//                    route = Routes.FinancialReports,
//                    icon = Icons.Outlined.BarChart
//                ),
//                MenuItemData(
//                    title = "Sistema de lealtad",
//                    route = Routes.LoyaltySystem,
//                    icon = Icons.Outlined.Star
//                ),
//                MenuItemData(
//                    title = "Promociones",
//                    route = Routes.Promotions,
//                    icon = Icons.Outlined.Discount
//                ),
//                MenuItemData(
//                    title = "Devoluciones",
//                    route = Routes.Returns,
//                    icon = Icons.Outlined.Replay
//                ),
//
//
//            )
//
//            var menuItemSelected by remember { mutableStateOf(Routes.Dashboard.route) }
//
//            Navigator(screen = DashboardScreen()) { navigator ->
//
//                val currentScreen = navigator.lastItem
//
//                Box(modifier = Modifier.fillMaxSize().background(FullCard)) {
//                    Column(
//                        modifier = Modifier
//                            .safeContentPadding()
//                            .fillMaxSize(),
//                    ) {
//                        Scaffold(containerColor = FullCard) {
//                            Row(modifier = Modifier.fillMaxSize()) {
//                                Column {
//                                    MainHeader()
//                                    Row {
//                                        SideBar(
//                                            menuItemList,
//                                            menuItemSelected
//                                        ) { newMenuItemSelected ->
//                                            menuItemSelected = newMenuItemSelected
//                                            when (menuItemSelected) {
//                                                Routes.Dashboard.route -> if (currentScreen !is DashboardScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(DashboardScreen())
//                                                }
//
//                                                Routes.Daily.route -> if (currentScreen !is DailyScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(DailyScreen())
//                                                }
//
//                                                Routes.Products.route -> if (currentScreen !is ProductsScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(ProductsScreen())
//                                                }
//                                                Routes.ProductVariants.route -> if(currentScreen !is ProductVariantsScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(ProductVariantsScreen())
//                                                }
//                                                Routes.Clients.route -> if (currentScreen !is ClientsListScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(ClientsListScreen())
//                                                }
//
//                                                Routes.CurrentAccounts.route -> if (currentScreen !is CurrentAccountsListScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(CurrentAccountsListScreen())
//                                                }
//
//                                                Routes.Suppliers.route -> if (currentScreen !is SuppliersListScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(SuppliersListScreen())
//                                                }
//
//                                                Routes.Sells.route -> if (currentScreen !is SellsListScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(SellsListScreen())
//                                                }
//
//                                                Routes.PendingOrders.route -> if (currentScreen !is PendingOrdersScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(PendingOrdersScreen())
//                                                }
//
//                                                Routes.Expenses.route -> if (currentScreen !is ExpensesScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(ExpensesScreen())
//                                                }
//
//                                                Routes.FinancialReports.route -> if (currentScreen !is FinancialReportsScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(FinancialReportsScreen())
//                                                }
//
//                                                Routes.LoyaltySystem.route -> if (currentScreen !is LoyaltySystemScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(LoyaltySystemScreen())
//                                                }
//
//                                                Routes.Promotions.route -> if (currentScreen !is PromotionsScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(PromotionsScreen())
//                                                }
//
//                                                Routes.Returns.route -> if (currentScreen !is ReturnsScreen) {
//                                                    navigator.popUntilRoot()
//                                                    navigator.replace(ReturnsScreen())
//                                                }
//                                            }
//                                        }
//                                        FadeTransition(navigator)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Aasdsad(menuItemList: List<MenuItemData>, menuItemSelected: String, onClick: (String) -> Unit) {
//    Box(modifier = Modifier.background(FullCard)) {
//        Column(
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            menuItemList.forEach { item ->
//                MenuItem(
//                    item, menuItemSelected,
//                ) { newMenuItem ->
//                    onClick(newMenuItem)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun Adsad(
//    itemData: MenuItemData,
//    menuItemSelected: String,
//    onClick: (String) -> Unit,
//) {
//
//    val interactionSource = remember { MutableInteractionSource() }
//    val isHovered by interactionSource.collectIsHoveredAsState()
//
//    Box(
//        modifier =
//            Modifier
//                .background(
//                    if (menuItemSelected == itemData.route.route) GreenText.copy(
//                        alpha = .3f
//                    ) else if (isHovered) PrimaryCardBackground else Color.Transparent
//                )
//                .hoverable(interactionSource)
//                .clickable { onClick(itemData.route.route) }
//                .pointerHoverIcon(PointerIcon.Hand)
//
//    ) {
//        Row(
//            modifier = Modifier.padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Icon(itemData.icon, contentDescription = "menu item", tint = Color.White)
//            Text(itemData.title, color = Color.White, fontSize = 14.sp)
//        }
//    }
//}
//
