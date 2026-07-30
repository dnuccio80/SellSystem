package org.example.project.ui.screens.addproducts

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.domain.models.Product
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.CheckBoxItem
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SimpleGenericHeader
import org.example.project.ui.screens.addproducts.UpdateProductAction.*
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.koin.compose.viewmodel.koinViewModel


enum class UpdateProductAction {
    NAME, DESCRIPTION, BRAND, BUY_PRICE, CHANGE_LIST_PRICE_SELECTION, LIST_PRICE, CHANGE_CASH_PRICE_SELECTION, CASH_PRICE, CATEGORY, CURRENT_STOCK, ADVICE_STOCK, TOGGLE_MANAGE_STOCK, TOGGLE_VARIANT_PRODUCT
}

class AddProductScreen(val productId: Int = 0) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<AddProductViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var adviceMsg by rememberSaveable { mutableStateOf("") }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(viewModel.events) {
            viewModel.events.collect { msg ->
                adviceMsg = msg
                showAdviceDialog = true
            }
        }

        LaunchedEffect(productId) {
            if (productId != 0) {
                viewModel.loadProduct(productId)
            }
        }

        when (uiState) {
            is AddProductUiState.Error -> {
                ScreenContainer {
                    Text(
                        "Ha ocurrido un error",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }

            is AddProductUiState.Success -> {

                val state = uiState as AddProductUiState.Success

                ScreenContainer {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (productId == 0) {
                            SimpleGenericHeader(
                                title = "Nuevo producto",
                                description = "Agrega información para agregar un nuevo producto"
                            )
                        } else {
                            GenericHeaderWithButtonAndSearch(
                                title = "Modificar producto",
                                buttonColor = AccentColor,
                                description = "Modifica los datos del producto",
                                buttonText = "Eliminar",
                                hasSearch = false,
                                onButtonClick = { showConfirmDialog = true }
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DataItem(
                                product = state.product,
                                onActionDone = { action, value ->
                                    when (action) {
                                        NAME -> viewModel.updateProduct(
                                            UpdatableProductData.NAME,
                                            value
                                        )

                                        DESCRIPTION -> viewModel.updateProduct(
                                            UpdatableProductData.DESCRIPTION,
                                            value
                                        )

                                        BRAND -> viewModel.updateProduct(
                                            UpdatableProductData.BRAND,
                                            value
                                        )

                                        CATEGORY -> viewModel.updateProduct(
                                            UpdatableProductData.CATEGORY,
                                            value
                                        )

                                        else -> {}
                                    }
                                }
                            )
                            StockAndVariantItem(
                                product = state.product,
                                manageStock = state.product.manageStock,
                                isVariableProduct = state.hasVariants,
                                onActionDone = { action, value ->
                                    when (action) {
                                        TOGGLE_MANAGE_STOCK -> viewModel.updateProduct(
                                            UpdatableProductData.TOGGLE_MANAGE_STOCK,
                                            value
                                        )

                                        CURRENT_STOCK -> viewModel.updateProduct(
                                            UpdatableProductData.CURRENT_STOCK,
                                            value
                                        )

                                        ADVICE_STOCK -> viewModel.updateProduct(
                                            UpdatableProductData.ADVICE_STOCK,
                                            value
                                        )

                                        TOGGLE_VARIANT_PRODUCT -> viewModel.updateProduct(
                                            UpdatableProductData.TOGGLE_HAS_VARIANTS,
                                            value
                                        )

                                        else -> {}
                                    }
                                }
                            )
                            PriceItem(
                                state = state,
                                onActionDone = { action, value ->
                                    when (action) {
                                        BUY_PRICE -> viewModel.updateProduct(
                                            UpdatableProductData.BUY_PRICE,
                                            value
                                        )

                                        CHANGE_LIST_PRICE_SELECTION -> {
                                            viewModel.updateProduct(
                                                UpdatableProductData.LIST_PRICE,
                                                ""
                                            )
                                            viewModel.changeListPriceSelection(value)
                                        }
                                        LIST_PRICE -> viewModel.updateProduct(
                                            UpdatableProductData.LIST_PRICE,
                                            value
                                        )
                                        CHANGE_CASH_PRICE_SELECTION -> {
                                            viewModel.updateProduct(
                                                UpdatableProductData.CASH_PRICE,
                                                ""
                                            )
                                            viewModel.changeCashPriceSelection(value)
                                        }

                                        CASH_PRICE -> viewModel.updateProduct(
                                            UpdatableProductData.CASH_PRICE,
                                            value
                                        )
                                        else -> {}
                                    }

                                }
                            )
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                AcceptDeclineButtons(
                                    onAccept = { viewModel.tryAddProduct { isEdit -> if(isEdit) navigator?.pop() } },
                                    onDismiss = {
                                        navigator?.pop()
                                        viewModel.cleanProductData()
                                    })
                            }
                        }
                    }
                    SimpleAdviceDialog(
                        msg = adviceMsg,
                        show = showAdviceDialog,
                        onDismiss = { showAdviceDialog = false }
                    )
                    if (showConfirmDialog) {
                        ConfirmDialog(
                            msg = "Sguro que deseas eliminar el producto?",
                            onAccept = {
                                viewModel.deleteProduct {
                                    showConfirmDialog = false
                                    navigator?.pop()
                                }
                            },
                            onDismiss = { showConfirmDialog = false }
                        )
                    }

                }
            }

            is AddProductUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = GreenText)
                }
            }
        }


    }
}

@Composable
fun StockAndVariantItem(
    product: Product,
    manageStock: Boolean,
    isVariableProduct: Boolean,
    onActionDone: (UpdateProductAction, String) -> Unit,
) {

    val currentStock = if (product.currentStock == 0) "" else product.currentStock.toString()
    val adviceStock = if (product.adviceStock == 0) "" else product.adviceStock.toString()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    if (product.id == 0) "Stock y variantes" else "Stock",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            CheckBoxItem("Gestionar stock", manageStock) {
                onActionDone(TOGGLE_MANAGE_STOCK, "")
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
            if (product.id != 0) return@Column
            CheckBoxItem(
                "Producto con variantes",
                isVariableProduct
            ) { onActionDone(TOGGLE_VARIANT_PRODUCT, "") }
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
        }
    }
}

@Composable
private fun PriceItem(
    state: AddProductUiState.Success,
    onActionDone: (UpdateProductAction, String) -> Unit,
) {

    val product = state.product

    val buyPrice = if (product.buyPrice == 0L) "" else product.buyPrice.toString()
    val listPrice = if (product.listPrice == 0L) "" else product.listPrice.toString()
    val cashPrice = if (product.cashPrice == 0L) "" else product.cashPrice.toString()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "Costos y precios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            GenericTextField(
                buyPrice,
                "Precio de compra",
                onlyNumbers = true,
                isPrice = true,
            ) { onActionDone(BUY_PRICE, it) }
            Spacer(Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Column {

                        Text(
                            "Precio de lista",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.priceListType.forEachIndexed { index, value ->
                                RadioButtonRowWithText(
                                    name = value,
                                    selected = state.priceListTypeSelected,
                                    onClick = {
                                        onActionDone(CHANGE_LIST_PRICE_SELECTION, "$index")
                                    }
                                )
                            }
                        }
                        AnimatedContent(state.priceListTypeSelected) {
                            if (state.priceListTypeSelected == state.priceListType.first()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    GenericTextField(
                                        listPrice,
                                        "Precio de lista",
                                        onlyNumbers = true,
                                        modifier = Modifier.weight(1f),
                                        isPrice = true,
                                    ) { onActionDone(LIST_PRICE, it) }
                                    Box(
                                        modifier = Modifier.background(GreenText),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "54%",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    GenericTextField(
                                        listPrice,
                                        "Porcentaje de ganancia",
                                        onlyNumbers = true,
                                        modifier = Modifier.weight(1f),
                                        isPercentAdd = true
                                    ) { onActionDone(LIST_PRICE, it) }
                                    Box(
                                        modifier = Modifier.background(GreenText),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "$15.000",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }

                                }
                            }

                        }
                    }
                }
                VerticalDivider(thickness = 2.dp, color = GrayText)
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(
                            "Precio en efectivo o transferencia",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.cashPriceType.forEachIndexed { index, value ->
                                RadioButtonRowWithText(
                                    name = value,
                                    selected = state.cashPriceTypeSelected,
                                    onClick = {
                                        onActionDone(CHANGE_CASH_PRICE_SELECTION, "$index")
                                    }
                                )
                            }
                        }
                        AnimatedContent(state.cashPriceTypeSelected) {
                            if (state.cashPriceTypeSelected == state.cashPriceType.first()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    GenericTextField(
                                        cashPrice,
                                        "Precio en efectivo/transferencia",
                                        onlyNumbers = true,
                                        modifier = Modifier.weight(1f),
                                        isPrice = true,
                                    ) { onActionDone(CASH_PRICE, it) }
                                    Box(
                                        modifier = Modifier.background(GreenText),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "54%",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }

                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    GenericTextField(
                                        cashPrice,
                                        "Descuento a aplicar por efectivo/transferencia",
                                        modifier = Modifier.weight(1f),
                                        onlyNumbers = true,
                                        isPercentOff = true
                                    ) { onActionDone(CASH_PRICE, it) }
                                    Box(
                                        modifier = Modifier.background(GreenText),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "$15.000",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            modifier = Modifier.padding(16.dp)
                                        )
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
private fun DataItem(
    product: Product,
    onActionDone: (UpdateProductAction, String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "Datos del producto",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column {
                GenericTextField(product.name, "Nombre") { onActionDone(NAME, it) }
                GenericTextField(product.description, "Descripción") {
                    onActionDone(
                        DESCRIPTION,
                        it
                    )
                }
                GenericTextField(product.brand, "Marca") { onActionDone(BRAND, it) }
                GenericTextField(product.category, "Categoría") { onActionDone(CATEGORY, it) }
            }

        }
    }
}