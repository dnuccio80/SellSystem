package org.example.project.ui.screens.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.domain.models.Product
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.CheckBoxItem
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.screens.products.UpdateProductAction.NAME
import org.example.project.ui.screens.products.UpdateProductAction.ADVICE_STOCK
import org.example.project.ui.screens.products.UpdateProductAction.BRAND
import org.example.project.ui.screens.products.UpdateProductAction.BUY_PRICE
import org.example.project.ui.screens.products.UpdateProductAction.CASH_PRICE
import org.example.project.ui.screens.products.UpdateProductAction.CATEGORY
import org.example.project.ui.screens.products.UpdateProductAction.CURRENT_STOCK
import org.example.project.ui.screens.products.UpdateProductAction.DESCRIPTION
import org.example.project.ui.screens.products.UpdateProductAction.LIST_PRICE
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel

class ProductsScreen : Screen {
    @Composable
    override fun Content() {

        val viewmodel = koinViewModel<ProductsViewModel>()
        val products by viewmodel.products.collectAsStateWithLifecycle()
        val product by viewmodel.product.collectAsStateWithLifecycle()
        val manageStock by viewmodel.manageStock.collectAsStateWithLifecycle()
        val query by viewmodel.query.collectAsStateWithLifecycle()
        var adviceMsg by rememberSaveable { mutableStateOf("") }

        var showAddProductDialog by rememberSaveable { mutableStateOf(false) }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var isVariableProduct by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(viewmodel.events) {
            viewmodel.events.collect { msg ->
                adviceMsg = msg
                showAdviceDialog = true
            }
        }



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
                ) { showAddProductDialog = true }
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

            if (showAddProductDialog) {
                AddProductDialog(
                    product,
                    manageStock = manageStock,
                    isVariableProduct = isVariableProduct,
                    onToggleManageStock = { viewmodel.toggleManageStock() },
                    onToggleVariableProduct = { isVariableProduct = !isVariableProduct },
                    onDismiss = {
                        viewmodel.cleanProductData()
                        showAddProductDialog = false
                    },
                    onAccept = {
                        viewmodel.tryAddProduct() { showAddProductDialog = false }
                    },
                    onActionDone = { action, value ->
                        when (action) {
                            NAME -> viewmodel.updateProduct(data = UpdatableProductData.NAME, value)
                            DESCRIPTION -> viewmodel.updateProduct(
                                data = UpdatableProductData.DESCRIPTION,
                                value = value
                            )

                            BRAND -> viewmodel.updateProduct(
                                data = UpdatableProductData.BRAND,
                                value = value
                            )

                            BUY_PRICE -> viewmodel.updateProduct(
                                data = UpdatableProductData.BUY_PRICE,
                                value = value
                            )

                            LIST_PRICE -> viewmodel.updateProduct(
                                data = UpdatableProductData.LIST_PRICE,
                                value = value
                            )

                            CASH_PRICE -> viewmodel.updateProduct(
                                data = UpdatableProductData.CASH_PRICE,
                                value = value
                            )

                            CATEGORY -> viewmodel.updateProduct(
                                data = UpdatableProductData.CATEGORY,
                                value = value
                            )

                            CURRENT_STOCK -> viewmodel.updateProduct(
                                data = UpdatableProductData.CURRENT_STOCK,
                                value = value
                            )

                            ADVICE_STOCK -> viewmodel.updateProduct(
                                data = UpdatableProductData.ADVICE_STOCK,
                                value = value
                            )
                        }
                    }
                )
            }
            if (showAddProductDialog) {
                SimpleAdviceDialog(adviceMsg, showAdviceDialog) { showAdviceDialog = false }
            }
        }
    }
}

enum class UpdateProductAction {
    NAME, DESCRIPTION, BRAND, BUY_PRICE, LIST_PRICE, CASH_PRICE, CATEGORY, CURRENT_STOCK, ADVICE_STOCK
}

@Composable
private fun AddProductDialog(
    product: Product,
    manageStock: Boolean,
    onActionDone: (UpdateProductAction, String) -> Unit,
    isVariableProduct: Boolean,
    onToggleManageStock: () -> Unit,
    onToggleVariableProduct: () -> Unit,
    onAccept:() -> Unit,
    onDismiss: () -> Unit,
) {

    val buyPrice = if (product.buyPrice == 0L) "" else product.buyPrice.toString()
    val listPrice = if (product.listPrice == 0L) "" else product.listPrice.toString()
    val cashPrice = if (product.cashPrice == 0L) "" else product.cashPrice.toString()
    val currentStock = if (product.currentStock == 0) "" else product.currentStock.toString()
    val adviceStock = if (product.adviceStock == 0) "" else product.adviceStock.toString()

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth().height(550.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Agregar nuevo producto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier.verticalScroll(
                        rememberScrollState()
                    )
                ) {
                    GenericTextField(product.name, "Nombre") { onActionDone(NAME, it) }
                    GenericTextField(product.description, "Descripción") {
                        onActionDone(
                            DESCRIPTION,
                            it
                        )
                    }
                    GenericTextField(product.brand, "Marca") { onActionDone(BRAND, it) }
                    GenericTextField(
                        buyPrice,
                        "Precio de compra",
                        onlyNumbers = true
                    ) { onActionDone(BUY_PRICE, it) }
                    GenericTextField(
                        listPrice,
                        "Precio de lista",
                        onlyNumbers = true
                    ) { onActionDone(LIST_PRICE, it) }
                    GenericTextField(
                        cashPrice,
                        "Precio en efectivo/transferencia",
                        onlyNumbers = true
                    ) { onActionDone(CASH_PRICE, it) }
                    GenericTextField(product.category, "Categoría") { onActionDone(CATEGORY, it) }
                    CheckBoxItem("Gestionar stock", manageStock) {
                        onToggleManageStock()
                    }
                    AnimatedContent(manageStock) {
                        if (manageStock) {
                            Column {
                                GenericTextField(
                                    currentStock,
                                    "Stock",
                                    onlyNumbers = true
                                ) { onActionDone(CURRENT_STOCK, it) }
                                GenericTextField(
                                    adviceStock,
                                    "Cantidad para notificar poco stock", onlyNumbers = true
                                ) { onActionDone(ADVICE_STOCK, it) }
                            }
                        }
                    }
                    CheckBoxItem(
                        "Producto con variantes",
                        isVariableProduct
                    ) { onToggleVariableProduct() }
                    AnimatedContent(isVariableProduct) {
                        if (isVariableProduct) {
                            Column(Modifier.padding(horizontal = 16.dp)) {
                                CheckBoxItem(
                                    "Gestionar color",
                                    checked = false,
                                    onClick = { },
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Margen de ganancia:",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "54%",
                                style = MaterialTheme.typography.titleMedium,
                                color = GreenText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    AcceptDeclineButtons(onAccept = { onAccept() }, onDismiss = { onDismiss() })
                }

            }
        }

    }
}



