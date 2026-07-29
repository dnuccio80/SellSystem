package org.example.project.ui.screens.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.ScreenContainer
import org.example.project.ui.screens.addproducts.AddProductScreen
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel

class ProductsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewmodel = koinViewModel<ProductsViewModel>()
        val products by viewmodel.products.collectAsStateWithLifecycle()
        val query by viewmodel.query.collectAsStateWithLifecycle()
        var adviceMsg by rememberSaveable { mutableStateOf("") }

        var showAddProductDialog by rememberSaveable { mutableStateOf(false) }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                GenericHeaderWithButtonAndSearch(
                    title = "Productos",
                    description = "Listado de todos los productos con y sin stock",
                    searchValue = query,
                    onSearchValueChange = { viewmodel.updateQuery(it) },
                    onDeleteQuerySearch = { viewmodel.updateQuery("") },
                    buttonText = "Agregar producto"
                ) { navigator?.push(AddProductScreen()) }
                if (products.isNotEmpty()) {
                    products.forEach { item ->
                        Text(item.name)
                    }
                } else {
                    Text(
                        "No hay productos disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        color = WhiteText
                    )
                }
            }

//            if (showAddProductDialog) {
//                AddProductDialog(
//                    product,
//                    manageStock = manageStock,
//                    isVariableProduct = isVariableProduct,
//                    onToggleManageStock = { viewmodel.toggleManageStock() },
//                    onToggleVariableProduct = { isVariableProduct = !isVariableProduct },
//                    onDismiss = {
//                        viewmodel.cleanProductData()
//                        showAddProductDialog = false
//                    },
//                    onAccept = {
//                        viewmodel.tryAddProduct() { showAddProductDialog = false }
//                    },
//                    onActionDone = { action, value ->
//                        when (action) {
//                            NAME -> viewmodel.updateProduct(data = UpdatableProductData.NAME, value)
//                            DESCRIPTION -> viewmodel.updateProduct(
//                                data = UpdatableProductData.DESCRIPTION,
//                                value = value
//                            )
//
//                            BRAND -> viewmodel.updateProduct(
//                                data = UpdatableProductData.BRAND,
//                                value = value
//                            )
//
//                            BUY_PRICE -> viewmodel.updateProduct(
//                                data = UpdatableProductData.BUY_PRICE,
//                                value = value
//                            )
//
//                            LIST_PRICE -> viewmodel.updateProduct(
//                                data = UpdatableProductData.LIST_PRICE,
//                                value = value
//                            )
//
//                            CASH_PRICE -> viewmodel.updateProduct(
//                                data = UpdatableProductData.CASH_PRICE,
//                                value = value
//                            )
//
//                            CATEGORY -> viewmodel.updateProduct(
//                                data = UpdatableProductData.CATEGORY,
//                                value = value
//                            )
//
//                            CURRENT_STOCK -> viewmodel.updateProduct(
//                                data = UpdatableProductData.CURRENT_STOCK,
//                                value = value
//                            )
//
//                            ADVICE_STOCK -> viewmodel.updateProduct(
//                                data = UpdatableProductData.ADVICE_STOCK,
//                                value = value
//                            )
//                        }
//                    }
//                )
//            }
            if (showAddProductDialog) {
                SimpleAdviceDialog(adviceMsg, showAdviceDialog) { showAdviceDialog = false }
            }
        }
    }
}



