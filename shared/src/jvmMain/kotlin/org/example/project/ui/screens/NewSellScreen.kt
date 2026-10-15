package org.example.project.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.domain.models.client.Client
import org.example.project.domain.models.product.ProductWithPromotion
import org.example.project.domain.models.promotions.PromotionType
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SearchTextField
import org.example.project.ui.ext.toPrice
import org.example.project.ui.models.ProductWithQuantityPresentation
import org.example.project.ui.screens.ItemSellActions.*
import org.example.project.ui.screens.clients.CleanClient
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.screens.newsell.NewSellUiState
import org.example.project.ui.screens.newsell.NewSellViewModel
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel


class NewSellScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<NewSellViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val clientSearchQuery by viewModel.clientSearchQuery.collectAsStateWithLifecycle()

        when (uiState) {
            is NewSellUiState.Error -> {}
            NewSellUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is NewSellUiState.Success -> {


                ScreenContainer {
                    var showClientDialog by rememberSaveable { mutableStateOf(false) }
                    var showAddItemsDialog by rememberSaveable { mutableStateOf(false) }
                    val isUsualClient by viewModel.isUsualClient.collectAsStateWithLifecycle()
                    var currentAccountClientSelected by rememberSaveable {
                        mutableStateOf(
                            CleanClient().getCleanClient()
                        )
                    }
                    val clientSelected by viewModel.clientSelected.collectAsStateWithLifecycle()
                    var adviceMsg by rememberSaveable { mutableStateOf("") }
                    var showAdviceDialog by rememberSaveable { mutableStateOf(false) }

                    val navigator = LocalNavigator.current

                    LaunchedEffect(viewModel.events) {
                        viewModel.events.collect { msg ->
                            adviceMsg = msg
                            showAdviceDialog = true
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        GenericHeaderWithButtonAndSearch(
                            title = "Nueva venta",
                            description = "Detalles de nueva venta",
                            buttonText = "Agregar items",
                            hasSearch = false
                        ) {
                            showAddItemsDialog = true
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            border = BorderStroke(2.dp, color = GrayText),
                            colors = cardColors(containerColor = Color.Transparent),
                        ) {
                            AnimatedContent((uiState as NewSellUiState.Success).productWithQuantityList.isNotEmpty()) { notEmpty ->
                                if (notEmpty) {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(16.dp).animateContentSize()
                                    ) {
                                        items((uiState as NewSellUiState.Success).productWithQuantityList) { productWithQuantity ->
                                            NewItemSell(
                                                productWithQuantity,
                                                onValueChange = { newValue ->
                                                    viewModel.manualQuantityChange(
                                                        productWithQuantity.productWithPromo.product.id,
                                                        newValue
                                                    )
                                                }
                                            ) { action ->
                                                when (action) {
                                                    INCREASE_QUANTITY -> viewModel.increaseProductQuantity(
                                                        productWithQuantity.productWithPromo.product.id
                                                    )

                                                    DECREASE_QUANTITY -> viewModel.decreaseProductQuantity(
                                                        productWithQuantity.productWithPromo.product.id
                                                    )

                                                    DELETE_ITEM -> viewModel.deleteProductWithQuantity(
                                                        productWithQuantity.productWithPromo.product.id
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        "No hay items añadidos",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = WhiteText,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            val paymentMethods = listOf(
                                PaymentMethod.CASH,
                                PaymentMethod.TRANSFER,
                                PaymentMethod.CARD,
                                PaymentMethod.CURRENT_ACCOUNT,
                            )

                            Text(
                                "Método de pago:",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                textDecoration = TextDecoration.Underline
                            )
                            paymentMethods.forEach {
                                RadioButtonRowWithText(
                                    name = it.etiquette,
                                    selected = (uiState as NewSellUiState.Success).paymentMethod.etiquette,
                                    onClick = { viewModel.updatePaymentMethod(it) }
                                )
                            }

                        }
                        AnimatedContent((uiState as NewSellUiState.Success).paymentMethod) {
                            if (it != PaymentMethod.CURRENT_ACCOUNT) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Row(
                                        modifier = Modifier.clickable {
                                            viewModel.toggleIsUsualClient()
                                        },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isUsualClient,
                                            onCheckedChange = {
                                                viewModel.toggleIsUsualClient()
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = GreenText,
                                                uncheckedColor = GrayText
                                            )
                                        )
                                        Text(
                                            "Es cliente usual",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    AnimatedContent(isUsualClient) {
                                        if (isUsualClient) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                            ) {
                                                GenericButton("Seleccionar cliente") {
                                                    showClientDialog = true
                                                }
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Text(
                                                        if (clientSelected != null) {
                                                            "Cliente seleccionado:"
                                                        } else {
                                                            "Selecciona un cliente"
                                                        },
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = if (clientSelected == null) AccentColor else Color.White
                                                    )
                                                    Text(
                                                        clientSelected?.fullName.orEmpty(),
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = GreenText
                                                    )
                                                    if (clientSelected != null) {
                                                        IconButton(
                                                            onClick = { viewModel.clearClientSelected() },
                                                            modifier = Modifier.size(18.dp),
                                                            colors = IconButtonDefaults.iconButtonColors(
                                                                containerColor = AccentColor,
                                                                contentColor = Color.White
                                                            )
                                                        ) {
                                                            Icon(
                                                                Icons.Default.Close,
                                                                modifier = Modifier.padding(4.dp),
                                                                contentDescription = "Delete client"
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    GenericButton("Seleccionar cliente") {
                                        showClientDialog = true
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            if (clientSelected != null) {
                                                "Cliente seleccionado:"
                                            } else {
                                                "Selecciona un cliente"
                                            },
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (clientSelected == null) AccentColor else Color.White
                                        )
                                        Text(
                                            clientSelected?.fullName.orEmpty(),
                                            fontWeight = FontWeight.SemiBold,
                                            color = GreenText
                                        )
                                        if (clientSelected != null) {
                                            Card(
                                                shape = CircleShape,
                                                colors = cardColors(
                                                    containerColor = AccentColor,
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                IconButton(
                                                    onClick = { viewModel.clearClientSelected() },
                                                    modifier = Modifier.size(18.dp),
                                                    colors = IconButtonDefaults.iconButtonColors(
                                                        containerColor = AccentColor,
                                                        contentColor = Color.White
                                                    )
                                                ) {
                                                    Icon(
                                                        Icons.Default.Close,
                                                        modifier = Modifier.padding(4.dp),
                                                        contentDescription = "Delete client"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(
                                Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    colors = cardColors(containerColor = SecondaryCardBackground),
                                    modifier = Modifier.width(500.dp)
                                ) {
                                    Column(
                                        Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(
                                                "Subtotal:",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                (uiState as NewSellUiState.Success).amounts.subtotal.toPrice(),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = GreenText,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(
                                                "Descuento pago en efectivo/transferencia:",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                (uiState as NewSellUiState.Success).amounts.paymentDiscounts.toPrice(),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = GreenText,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(
                                                "Descuento por promociones vigentes:",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                (uiState as NewSellUiState.Success).amounts.promoDiscounts.toPrice(),
                                                style = MaterialTheme.typography.titleSmall,
                                                color = GreenText,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.size(0.dp))
                                        HorizontalDivider(
                                            thickness = 1.5.dp,
                                            color = GrayText
                                        )
                                        Spacer(modifier = Modifier.size(0.dp))
                                        Box(
                                            Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    8.dp
                                                )
                                            ) {
                                                Text(
                                                    "Total:",
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    (uiState as NewSellUiState.Success).amounts.total.toPrice(),
                                                    style = MaterialTheme.typography.headlineSmall,
                                                    color = GreenText,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                    }
                                }

                            }
                            Box(
                                Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                AcceptDeclineButtons(
                                    onDismiss = { navigator?.pop() },
                                    onAccept = { viewModel.addSell() { navigator?.pop() } })
                            }
                        }
                    }

                    if (showClientDialog) {
                        DialogContent(
                            textFieldValue = clientSearchQuery,
                            onTextFieldValueChange = { viewModel.updateClientSearchQuery(it) },
                            content = {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    if ((uiState as NewSellUiState.Success).clients.isNotEmpty()) {
                                        (uiState as NewSellUiState.Success).clients.forEach {
                                            ClientItem(
                                                it,
                                                selected = currentAccountClientSelected,
                                                onClick = {
                                                    currentAccountClientSelected = it
                                                }
                                            )
                                        }
                                    } else {
                                        Text(
                                            "No hay clientes en la búsqueda",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = WhiteText
                                        )
                                    }

                                }
                            },
                            onAccept = {
                                if (currentAccountClientSelected.fullName.isNotBlank()) {
                                    showClientDialog = false
                                    viewModel.updateClientSelected(currentAccountClientSelected)
                                    viewModel.updateClientSearchQuery("")
                                    currentAccountClientSelected = CleanClient().getCleanClient()
                                }
                            }
                        ) {
                            showClientDialog = false
                            viewModel.updateClientSearchQuery("")
                            currentAccountClientSelected = CleanClient().getCleanClient()
                        }
                    }
                    if (showAddItemsDialog) {

                        val productsWithPromoList = mutableListOf<ProductWithPromotion>()

                        DialogContent(
                            textFieldValue = (uiState as NewSellUiState.Success).productQuery,
                            onTextFieldValueChange = { viewModel.updateQuery(it) },
                            content = {
                                if ((uiState as NewSellUiState.Success).productList.isNotEmpty()) {
                                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        items((uiState as NewSellUiState.Success).productList) { productWithPromo ->
                                            ProductItem(productWithPromo) { added ->
                                                if (added) {
                                                    productsWithPromoList.add(productWithPromo)
                                                } else {
                                                    productsWithPromoList.remove(productWithPromo)
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        "No hay productos disponibles en la búsqueda",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = WhiteText
                                    )
                                }
                            },
                            onAccept = {
                                viewModel.addProductToCart(productsWithPromoList)
                                showAddItemsDialog = false
                                viewModel.updateQuery("")

                            },
                        ) {
                            viewModel.updateQuery("")
                            showAddItemsDialog = false
                        }
                    }
                    SimpleAdviceDialog(
                        msg = adviceMsg,
                        show = showAdviceDialog,
                        onDismiss = { showAdviceDialog = false }
                    )
                }
            }
        }
    }
}


@Composable
private fun DialogContent(
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    content: @Composable () -> Unit,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().height(700.dp),
            colors = cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "close dialog",
                        tint = Color.White
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                SearchTextField(
                    textFieldValue,
                    modifier = Modifier.fillMaxWidth(),
                    onDelete = { onTextFieldValueChange("") },
                    onValueChange = { onTextFieldValueChange(it) }
                )

                Spacer(Modifier.size(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
                Spacer(modifier = Modifier.height(16.dp))
                AcceptDeclineButtons(onDismiss = { onDismiss() }, onAccept = { onAccept() })
            }
        }
    }
}


@Composable
private fun ClientItem(client: Client, selected: Client, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable {
            onClick()
        },
        shape = RoundedCornerShape(4.dp),
        colors = cardColors(
            containerColor = SecondaryCardBackground,
            contentColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = client.fullName == selected.fullName,
                    onClick = { onClick() },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = GreenText,
                        unselectedColor = GrayText
                    )
                )
                Text(
                    client.fullName,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Puntos:",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "1500",
                    color = GreenText,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}

@Composable
private fun ProductItem(productWithPromo: ProductWithPromotion, onProductAdd: (Boolean) -> Unit) {

    var productAdded by rememberSaveable { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val cardBackgroundColor = if (isHovered) GreenText.copy(.3f) else SecondaryCardBackground

    Card(
        modifier = Modifier.fillMaxWidth().hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand).clickable {
                productAdded = !productAdded
                onProductAdd(productAdded)
            },
        shape = RoundedCornerShape(4.dp),
        colors = cardColors(
            containerColor = cardBackgroundColor,
            contentColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = productAdded,
                    onCheckedChange = {
                        productAdded = !productAdded
                        onProductAdd(productAdded)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = GreenText,
                        uncheckedColor = GrayText
                    )
                )
                Text(
                    "${productWithPromo.product.name} '${productWithPromo.product.brand}' ${productWithPromo.product.description}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                productWithPromo.product.listPrice.toPrice(),
                color = GreenText,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

enum class ItemSellActions {
    INCREASE_QUANTITY, DECREASE_QUANTITY, DELETE_ITEM
}

@Composable
private fun NewItemSell(
    productWithQuantity: ProductWithQuantityPresentation,
    onValueChange: (Int) -> Unit,
    onActionDone: (ItemSellActions) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(5f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(5f)
            ) {
                Text(
                    "${productWithQuantity.productWithPromo.product.name} '${productWithQuantity.productWithPromo.product.brand}' ${productWithQuantity.productWithPromo.product.description}",
                    color = WhiteText,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                if (productWithQuantity.productWithPromo.promotion != null) {
                    val textValue =
                        when (productWithQuantity.productWithPromo.promotion.promotionType) {
                            PromotionType.BUY_X_PAY_Y -> {
                                "${productWithQuantity.productWithPromo.promotion.buyX} x ${productWithQuantity.productWithPromo.promotion.payY}"
                            }

                            PromotionType.PERCENT -> {
                                "${productWithQuantity.productWithPromo.promotion.percent}% off"
                            }
                        }
                    Card(
                        shape = RoundedCornerShape(4.dp),
                        colors = cardColors(containerColor = AccentColor)
                    ) {
                        Text(
                            textValue,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Row(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("Cant.", color = WhiteText, maxLines = 1)
                    IconButton(
                        onClick = {
                            if (productWithQuantity.quantity == 1) onActionDone(DELETE_ITEM)
                            else onActionDone(DECREASE_QUANTITY)
                        },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            if (productWithQuantity.quantity == 1) Icons.Outlined.Delete else Icons.Outlined.Remove,
                            contentDescription = "reduce by 1",
                            tint = if (productWithQuantity.quantity == 1) AccentColor else Color.White
                        )
                    }
                    TextField(
                        value = productWithQuantity.quantity.toString(), onValueChange = {
                            val value =
                                if (productWithQuantity.productWithPromo.product.manageStock) {
                                    (it.toIntOrNull() ?: 1).coerceIn(
                                        1,
                                        productWithQuantity.productWithPromo.product.currentStock
                                    )
                                } else {
                                    (it.toIntOrNull() ?: 1)
                                }

                            onValueChange(value)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = SecondaryCardBackground,
                            unfocusedContainerColor = SecondaryCardBackground,
                            focusedIndicatorColor = GreenText,
                            unfocusedIndicatorColor = GrayText,
                            unfocusedTextColor = WhiteText,
                            focusedTextColor = WhiteText,
                            cursorColor = GreenText
                        ),
                        modifier = Modifier.width(80.dp).height(55.dp),
                        singleLine = true,
                        maxLines = 1,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    )
                    IconButton(
                        onClick = { onActionDone(INCREASE_QUANTITY) },
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "plus by 1",
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(32.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.width(180.dp)
            ) {
                Text("Subtotal:", color = WhiteText, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(
                    productWithQuantity.amount.toPrice(),
                    color = GreenText,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

        }

    }
}
