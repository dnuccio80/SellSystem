package org.example.project.ui.screens.addproducts

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.CardTitleCentered
import org.example.project.ui.CheckBoxItem
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericSelectableTextField
import org.example.project.ui.GenericTextField
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.RowWithMidTitleAndDescription
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SimpleGenericHeader
import org.example.project.ui.ext.formatToDisplay
import org.example.project.ui.ext.toPercentAdd
import org.example.project.ui.ext.toPercentOff
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.addproducts.UpdateProductAction.ADVICE_STOCK
import org.example.project.ui.screens.addproducts.UpdateProductAction.BRAND
import org.example.project.ui.screens.addproducts.UpdateProductAction.BUY_PRICE
import org.example.project.ui.screens.addproducts.UpdateProductAction.CASH_PRICE
import org.example.project.ui.screens.addproducts.UpdateProductAction.CASH_PRICE_PERCENTAGE
import org.example.project.ui.screens.addproducts.UpdateProductAction.CATEGORY
import org.example.project.ui.screens.addproducts.UpdateProductAction.CHANGE_CASH_PRICE_SELECTION
import org.example.project.ui.screens.addproducts.UpdateProductAction.CHANGE_LIST_PRICE_SELECTION
import org.example.project.ui.screens.addproducts.UpdateProductAction.CURRENT_STOCK
import org.example.project.ui.screens.addproducts.UpdateProductAction.DESCRIPTION
import org.example.project.ui.screens.addproducts.UpdateProductAction.EXPIRE_DATE
import org.example.project.ui.screens.addproducts.UpdateProductAction.LIST_PRICE
import org.example.project.ui.screens.addproducts.UpdateProductAction.LIST_PRICE_PERCENTAGE
import org.example.project.ui.screens.addproducts.UpdateProductAction.NAME
import org.example.project.ui.screens.addproducts.UpdateProductAction.TOGGLE_MANAGE_EXPIRE_DATE
import org.example.project.ui.screens.addproducts.UpdateProductAction.TOGGLE_MANAGE_STOCK
import org.example.project.ui.screens.addproducts.UpdateProductAction.TOGGLE_VARIANT_PRODUCT
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel
import java.io.File
import kotlin.time.Clock
import kotlin.time.Instant


enum class UpdateProductAction {
    NAME, DESCRIPTION, BRAND, BUY_PRICE, CHANGE_LIST_PRICE_SELECTION, LIST_PRICE, LIST_PRICE_PERCENTAGE, CHANGE_CASH_PRICE_SELECTION, CASH_PRICE, CASH_PRICE_PERCENTAGE, CATEGORY, CURRENT_STOCK, ADVICE_STOCK, TOGGLE_MANAGE_STOCK, EXPIRE_DATE, TOGGLE_MANAGE_EXPIRE_DATE, TOGGLE_VARIANT_PRODUCT
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
                                categories = state.categories,
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
                                viewModel = viewModel,
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

                                        EXPIRE_DATE -> viewModel.updateProduct(
                                            UpdatableProductData.EXPIRE_DATE,
                                            value
                                        )

                                        TOGGLE_MANAGE_EXPIRE_DATE -> viewModel.updateProduct(
                                            UpdatableProductData.TOGGLE_MANAGE_EXPIRE_DATE, ""
                                        )

                                        else -> {}
                                    }
                                }
                            )
                            PriceItem(
                                state = state,
                                viewModel = viewModel,
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
                                            viewModel.updateProduct(
                                                UpdatableProductData.LIST_PRICE_PERCENTAGE,
                                                ""
                                            )
                                            viewModel.changeListPriceSelection(value)
                                        }

                                        LIST_PRICE -> viewModel.updateProduct(
                                            UpdatableProductData.LIST_PRICE,
                                            value
                                        )

                                        LIST_PRICE_PERCENTAGE -> viewModel.updateProduct(
                                            UpdatableProductData.LIST_PRICE_PERCENTAGE,
                                            value
                                        )

                                        CHANGE_CASH_PRICE_SELECTION -> {
                                            viewModel.updateProduct(
                                                UpdatableProductData.CASH_PRICE,
                                                ""
                                            )
                                            viewModel.updateProduct(
                                                UpdatableProductData.CASH_PRICE_PERCENTAGE,
                                                ""
                                            )
                                            viewModel.changeCashPriceSelection(value)
                                        }

                                        CASH_PRICE -> viewModel.updateProduct(
                                            UpdatableProductData.CASH_PRICE,
                                            value
                                        )

                                        CASH_PRICE_PERCENTAGE -> {
                                            viewModel.updateProduct(
                                                UpdatableProductData.CASH_PRICE_PERCENTAGE,
                                                value
                                            )
                                        }

                                        else -> {}
                                    }

                                }
                            )
                            ImageCardItem(
                                state.product,
                                onSelectImageClick = { viewModel.selectImage() },
                                onDeleteImage = {
                                    viewModel.updateProduct(
                                        UpdatableProductData.DELETE_IMAGE, ""
                                    )
                                })
                            ProfitResumeItem(state)
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                AcceptDeclineButtons(
                                    onAccept = { viewModel.tryAddProduct { isEdit -> if (isEdit) navigator?.pop() } },
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
    viewModel: AddProductViewModel,
    manageStock: Boolean,
    isVariableProduct: Boolean,
    onActionDone: (UpdateProductAction, String) -> Unit,
) {

    val currentStock = if (product.currentStock == 0) "" else product.currentStock.toString()
    val adviceStock = if (product.adviceStock == 0) "" else product.adviceStock.toString()

    val today = Clock.System
        .todayIn(TimeZone.currentSystemDefault())

    val selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val date = Instant
                .fromEpochMilliseconds(utcTimeMillis)
                .toLocalDateTime(TimeZone.UTC)
                .date

            return date > today
        }
    }

    val datePickerState = rememberDatePickerState(selectableDates = selectableDates)
    var showExpireDateDialog by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CardTitleCentered(if (product.id == 0) "Stock y variantes" else "Stock")
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
                        Spacer(Modifier.size(16.dp))

                    }
                }
            }
            CheckBoxItem(
                "Gestionar fecha de vencimiento",
                product.manageExpireDate
            ) { onActionDone(TOGGLE_MANAGE_EXPIRE_DATE, "") }
            AnimatedContent(product.manageExpireDate) {
                if (product.manageExpireDate) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val hasExpireDate = product.expireDate != null
                        GenericButton(text = "Agregar fecha") { showExpireDateDialog = true }
                        Text(
                            text = if (hasExpireDate) "Fecha: ${product.expireDate.formatToDisplay()}" else "Sin fecha de vencimiento",
                            fontWeight = FontWeight.SemiBold,
                            color = GreenText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(Modifier.size(16.dp))
                }
                DatePickerDialogItem(
                    show = showExpireDateDialog,
                    onDismiss = { showExpireDateDialog = false },
                    onConfirm = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date
                            viewModel.updateProduct(UpdatableProductData.EXPIRE_DATE, date)
                            showExpireDateDialog = false
                        }
                    },
                    datePickerState = datePickerState
                )
            }

            if (product.id != 0) return@Column
            CheckBoxItem(
                "Gestionar variantes",
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
@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerDialogItem(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    datePickerState: DatePickerState,
) {
    if (show) {
        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                GenericButton(
                    text = "Confirmar",
                    color = GreenText
                ) {
                    onConfirm()
                }
            },
            dismissButton = {
                GenericButton(
                    text = "Cancelar",
                    color = GrayText
                ) {
                    onDismiss()
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = PrimaryCardBackground
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = PrimaryCardBackground,
                    titleContentColor = WhiteText,
                    headlineContentColor = WhiteText,
                    weekdayContentColor = WhiteText,
                    subheadContentColor = WhiteText,
                    navigationContentColor = WhiteText,
                    yearContentColor = WhiteText,
                    currentYearContentColor = GreenText,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = GreenText,
                    dayContentColor = WhiteText,
                    selectedDayContentColor = Color.White,
                    disabledDayContentColor = GrayText,
                    disabledYearContentColor = GrayText,
                    selectedDayContainerColor = GreenText,
                    todayContentColor = WhiteText,
                    todayDateBorderColor = GrayText,
                    dividerColor = GrayText,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedPlaceholderColor = WhiteText,
                        unfocusedPlaceholderColor = WhiteText,
                        focusedTrailingIconColor = WhiteText,
                        unfocusedTrailingIconColor = WhiteText,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = GreenText,
                        unfocusedIndicatorColor = GrayText,
                        cursorColor = GreenText,
                        focusedLabelColor = GreenText,
                        unfocusedLabelColor = GrayText,
                        errorTextColor = WhiteText,
                        errorLabelColor = GreenText,
                        errorCursorColor = AccentColor,
                        errorSupportingTextColor = AccentColor,
                        errorContainerColor = PrimaryCardBackground,
                    ),
                )
            )
        }
    }
}

@Composable
private fun PriceItem(
    state: AddProductUiState.Success,
    viewModel: AddProductViewModel,
    onActionDone: (UpdateProductAction, String) -> Unit,
) {

    val product = state.product

    val buyPrice = if (product.buyPrice == 0L) "" else product.buyPrice.toString()
    val listPrice = if (product.listPrice == 0L) "" else product.listPrice.toString()
    val listPricePercentage =
        if (state.percentageListProfit == 0L) "" else state.percentageListProfit.toString()
    val cashPricePercentage =
        if (state.percentageCashDiscount == 0L) "" else state.percentageCashDiscount.toString()
    val cashPrice = if (product.cashPrice == 0L) "" else product.cashPrice.toString()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            CardTitleCentered("Costos y precios")

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
                            viewModel.priceListType.forEachIndexed { index, value ->
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
                            if (state.priceListTypeSelected == viewModel.priceListType.first()) {
                                GenericTextField(
                                    listPrice,
                                    "Precio de lista",
                                    onlyNumbers = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    isPrice = true,
                                ) { onActionDone(LIST_PRICE, it) }
                            } else {
                                GenericTextField(
                                    listPricePercentage,
                                    "Margen de ganancia (en porcentaje)",
                                    onlyNumbers = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    isPercentAdd = true
                                ) { onActionDone(LIST_PRICE_PERCENTAGE, it) }
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
                            viewModel.cashPriceType.forEachIndexed { index, value ->
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
                            if (state.cashPriceTypeSelected == viewModel.cashPriceType.first()) {
                                GenericTextField(
                                    cashPrice,
                                    "Precio en efectivo/transferencia",
                                    onlyNumbers = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    isPrice = true,
                                ) { onActionDone(CASH_PRICE, it) }
                            } else {
                                GenericTextField(
                                    cashPricePercentage,
                                    "Descuento a aplicar por efectivo/transferencia",
                                    modifier = Modifier.fillMaxWidth(),
                                    onlyNumbers = true,
                                    isPercentOff = true
                                ) { onActionDone(CASH_PRICE_PERCENTAGE, it) }
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
    categories:List<ProductCategory>,
    onActionDone: (UpdateProductAction, String) -> Unit,
) {

    var showDropdownMenuCategory by rememberSaveable { mutableStateOf(false) }

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
            CardTitleCentered("Datos del producto")
            Column {
                GenericTextField(product.name, "Nombre") { onActionDone(NAME, it) }
                GenericTextField(product.description, "Descripción") {
                    onActionDone(
                        DESCRIPTION,
                        it
                    )
                }
                GenericTextField(product.brand, "Marca") { onActionDone(BRAND, it) }
                Column {
                    GenericSelectableTextField(
                        value = product.category,
                        labelText = "Categoría",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showDropdownMenuCategory = true }
                    )
                    DropdownMenu(
                        expanded = showDropdownMenuCategory,
                        onDismissRequest = { showDropdownMenuCategory = false },
                        modifier = Modifier.background(SecondaryCardBackground).width(350.dp).heightIn(max = 200.dp),
                        scrollState = rememberScrollState()
                    ) {
                        if(categories.isNotEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Sin categoría") },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                onClick = {
                                    onActionDone(CATEGORY, "Sin categoría")
                                    showDropdownMenuCategory = false
                                },
                                colors = MenuItemColors(
                                    textColor = Color.White,
                                    leadingIconColor = Color.White,
                                    trailingIconColor = Color.White,
                                    disabledTextColor = Color.White,
                                    disabledLeadingIconColor = Color.White,
                                    disabledTrailingIconColor = Color.White,
                                )
                            )
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                    onClick = {
                                        onActionDone(CATEGORY, category.name)
                                        showDropdownMenuCategory = false
                                    },
                                    colors = MenuItemColors(
                                        textColor = Color.White,
                                        leadingIconColor = Color.White,
                                        trailingIconColor = Color.White,
                                        disabledTextColor = Color.White,
                                        disabledLeadingIconColor = Color.White,
                                        disabledTrailingIconColor = Color.White,
                                    )
                                )
                            }
                        } else {
                            DropdownMenuItem(
                                text = { Text("No hay categorías creadas") },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                onClick = { },
                                colors = MenuItemColors(
                                    textColor = Color.White,
                                    leadingIconColor = Color.White,
                                    trailingIconColor = Color.White,
                                    disabledTextColor = Color.White,
                                    disabledLeadingIconColor = Color.White,
                                    disabledTrailingIconColor = Color.White,
                                )
                            )
                        }


                    }
                }
            }
        }
    }
}

@Composable
private fun ProfitResumeItem(state: AddProductUiState.Success) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.elevatedCardElevation(16.dp),
            modifier = Modifier.widthIn(max = 450.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Resumen financiero",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
                    RowWithMidTitleAndDescription(
                        "Precio de compra:",
                        state.product.buyPrice.toPrice()
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RowWithMidTitleAndDescription(
                        "Precio de lista:",
                        state.product.listPrice.toPrice(),
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(GreenText)
                    ) {
                        Text(
                            state.percentageListProfit.toPercentAdd(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RowWithMidTitleAndDescription(
                        "Precio en efectivo/transferencia:",
                        state.product.cashPrice.toPrice(),
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(GreenText)
                    ) {
                        Text(
                            state.percentageCashDiscount.toPercentOff(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun ImageCardItem(
    product: Product,
    onSelectImageClick: () -> Unit,
    onDeleteImage: () -> Unit,
) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.elevatedCardElevation(16.dp),
            modifier = Modifier.width(450.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Imagen del producto",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                GenericButton(
                    "Seleccionar Imagen",
                    color = GreenText
                ) {
                    onSelectImageClick()
                }
                if (product.imagePath == null) {
                    Text(
                        "Ninguna imagen seleccionada",
                        color = WhiteText,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Box(Modifier.size(100.dp), contentAlignment = Alignment.TopEnd) {
                        AsyncImage(
                            model = File(product.imagePath),
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = AccentColor)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "eliminar imagen",
                                tint = Color.White,
                                modifier = Modifier.clickable { onDeleteImage() }.pointerHoverIcon(
                                    PointerIcon.Hand
                                )
                            )
                        }

                    }

                }
            }
        }
    }
}

