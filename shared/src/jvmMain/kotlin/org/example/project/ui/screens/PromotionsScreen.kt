package org.example.project.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.domain.models.promotions.PromotionCategory.*
import org.example.project.domain.models.promotions.PromotionType.*
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.CheckBoxItem
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.formatToDisplay
import org.example.project.ui.models.PromotionPresentation
import org.example.project.ui.screens.promotions.PromotionsViewModel
import org.example.project.ui.screens.promotions.UpdatePromotionAction
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.LightBlue
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.example.project.ui.utils.Yellowe
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant


class PromotionsScreen : Screen {
    @Composable
    override fun Content() {
        var showNewPromotionDialog by rememberSaveable { mutableStateOf(false) }
        val viewModel = koinViewModel<PromotionsViewModel>()
        val promotionData by viewModel.promotionData.collectAsStateWithLifecycle()
        val categories by viewModel.categories.collectAsStateWithLifecycle()
        val products by viewModel.products.collectAsStateWithLifecycle()

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Promociones",
                    description = "Listado de todas las promociones, vigentes y no vigentes",
                    buttonText = "Nueva promoción",
                    hasSearch = false
                ) { showNewPromotionDialog = true }
            }

            if (showNewPromotionDialog) {
                NewPromotionDialog(
                    promotionData = promotionData,
                    categories = categories,
                    products = products,
                    onDismiss = {
                        showNewPromotionDialog = false
                        viewModel.cleanPromotionData()
                    },
                    onActionDone = { action, value ->
                        viewModel.updatePromotionData(action, value)
                    },
                    onAddCategories = { list ->
                        viewModel.updatePromotionListSelected(list)
                    },
                    onAddProducts = { list ->
                        viewModel.updateProductsListSelected(list)
                    },
                    onAddDatePromoInit = { viewModel.updatePromoDateInit(it) },
                    onAddDatePromoEnd = { viewModel.updatePromoDateEnd(it) },
                    onToggleDateInit = { viewModel.toggleHasPromoDateInit() },
                    onToggleDateEnd = { viewModel.toggleHasPromoDateEnd() }
                )
            }
        }
    }
}

@Composable
private fun NewPromotionDialog(
    promotionData: PromotionPresentation,
    categories: List<ProductCategory>,
    products: List<Product>,
    onActionDone: (UpdatePromotionAction, String) -> Unit,
    onAddCategories: (List<ProductCategory>) -> Unit,
    onAddProducts: (List<Product>) -> Unit,
    onAddDatePromoInit: (LocalDateTime) -> Unit,
    onAddDatePromoEnd: (LocalDateTime) -> Unit,
    onToggleDateInit: () -> Unit,
    onToggleDateEnd: () -> Unit,
    onDismiss: () -> Unit,
) {

    val promotionType = listOf(
        BUY_X_PAY_Y,
        PERCENT
    )

    val promotionCategory = listOf(
        BRAND,
        CATEGORY,
        SPECIFIC
    )

    var showCategoryDialog by rememberSaveable { mutableStateOf(false) }
    var showProductDialog by rememberSaveable { mutableStateOf(false) }
    var showDateTimePickerBegins by rememberSaveable { mutableStateOf(false) }
    var showDateTimePickerEnds by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Gestionar nueva promoción",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column {
                    Text(
                        "Tipo de promoción",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        promotionType.forEach { promo ->
                            RadioButtonRowWithText(
                                promo.title,
                                selected = promotionData.promotionType.title,
                                onClick = {
                                    onActionDone(
                                        UpdatePromotionAction.PROMOTION_TYPE,
                                        promo.title
                                    )
                                },
                            )
                        }
                    }
                    AnimatedContent(promotionData.promotionType) {
                        when (promotionData.promotionType.title) {
                            BUY_X_PAY_Y.title -> {
                                Column {
                                    GenericTextField(
                                        value = if (promotionData.buyX == null) "" else promotionData.buyX.toString(),
                                        labelText = "Compra",
                                        onlyNumbers = true,
                                        onValueChange = {
                                            onActionDone(
                                                UpdatePromotionAction.BUY_X,
                                                it
                                            )
                                        }
                                    )
                                    GenericTextField(
                                        value = if (promotionData.payY == null) "" else promotionData.payY.toString(),
                                        labelText = "Paga",
                                        onValueChange = {
                                            onActionDone(
                                                UpdatePromotionAction.PAY_Y,
                                                it
                                            )
                                        }
                                    )
                                }
                            }

                            PERCENT.title -> {
                                GenericTextField(
                                    value = if (promotionData.percent == null) "" else promotionData.percent.toString(),
                                    labelText = "Porcentaje de descuento",
                                    onlyNumbers = true,
                                    isPercentOff = true,
                                    onValueChange = {
                                        onActionDone(
                                            UpdatePromotionAction.PERCENT,
                                            it
                                        )
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.size(16.dp))
                    Spacer(Modifier.size(16.dp))
                    Text(
                        "Seleccionar productos por:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(16.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        promotionCategory.forEach { category ->
                            RadioButtonRowWithText(
                                name = category.title,
                                selected = promotionData.promoteBy.title,
                                onClick = {
                                    onActionDone(
                                        UpdatePromotionAction.PROMOTION_CATEGORY,
                                        category.title
                                    )
                                }
                            )
                        }
                    }
                    AnimatedContent(promotionData.promoteBy.title) {
                        when (promotionData.promoteBy.title) {
                            CATEGORY.title -> {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    GenericButton(
                                        text = "Seleccionar categorías",
                                        icon = Icons.Outlined.KeyboardDoubleArrowRight,
                                        color = GreenText,
                                        onClick = { showCategoryDialog = true }
                                    )
                                    AnimatedContent(promotionData.categoryListSelected) {
                                        if (promotionData.categoryListSelected != null) {
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    "Categorías seleccionadas:",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.White
                                                )
                                                LazyVerticalGrid(
                                                    columns = GridCells.Adaptive(100.dp),
                                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    items(promotionData.categoryListSelected) {
                                                        Box(
                                                            contentAlignment = Alignment.Center,
                                                            modifier = Modifier.background(GreenText)
                                                        ) {
                                                            Text(
                                                                it.name,
                                                                modifier = Modifier.padding(4.dp),
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                color = Color.White,
                                                                fontWeight = FontWeight.SemiBold,
                                                                maxLines = 1
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            BRAND.title -> {
                                GenericTextField(
                                    value = promotionData.brandSelected.orEmpty(),
                                    labelText = "Marca",
                                    onValueChange = {
                                        onActionDone(
                                            UpdatePromotionAction.BRAND,
                                            it
                                        )
                                    }
                                )
                            }

                            SPECIFIC.title -> {
                                Column {
                                    GenericButton(
                                        text = "Seleccionar productos",
                                        icon = Icons.Outlined.KeyboardDoubleArrowRight,
                                        color = GreenText,
                                        onClick = { showProductDialog = true }
                                    )
                                    AnimatedContent(promotionData.categoryListSelected) {
                                        if (promotionData.specificProducts != null) {
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    "Productos seleccionados:",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.White
                                                )
                                                LazyVerticalGrid(
                                                    columns = GridCells.Adaptive(100.dp),
                                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    items(promotionData.specificProducts) {
                                                        Box(
                                                            contentAlignment = Alignment.Center,
                                                            modifier = Modifier.background(GreenText)
                                                        ) {
                                                            Text(
                                                                "${it.name} '${it.brand}' ${it.description}",
                                                                modifier = Modifier.padding(4.dp),
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                color = Color.White,
                                                                fontWeight = FontWeight.SemiBold,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
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
                }
                CheckBoxItem(
                    name = "Colocar fecha de inicio y fin",
                    checked = promotionData.hasDate,
                    onClick = { onActionDone(UpdatePromotionAction.TOGGLE_DATE, "") }
                )
                AnimatedContent(promotionData.hasDate) {
                    if (promotionData.hasDate) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                GenericButton(
                                    text = if (promotionData.dateInit != null) promotionData.dateInit.formatToDisplay() else "Seleccionar inicio",
                                    modifier = Modifier.weight(1f),
                                    enabled = promotionData.hasDateInit,
                                    onClick = { showDateTimePickerBegins = true }
                                )
                                CheckBoxItem(
                                    name = "Sin fecha de inicio",
                                    checked = !promotionData.hasDateInit,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onToggleDateInit() }
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                GenericButton(
                                    text = if (promotionData.dateEnd != null) promotionData.dateEnd.formatToDisplay() else "Seleccionar Finalización",
                                    enabled = promotionData.hasDateEnd,
                                    modifier = Modifier.weight(1f),
                                    onClick = { showDateTimePickerEnds = true }
                                )
                                CheckBoxItem(
                                    name = "Sin fecha de finalización",
                                    checked = !promotionData.hasDateEnd,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onToggleDateEnd() }
                                )
                            }
                        }
                    }
                }

                if (showCategoryDialog) {
                    CategoriesDialog(
                        categories,
                        onDismiss = { showCategoryDialog = false },
                        onAddCategories = {
                            onAddCategories(it)
                            showCategoryDialog = false
                        },
                    )
                }
                if (showProductDialog) {
                    ProductsDialog(
                        products = products,
                        onDismiss = { showProductDialog = false },
                        onAddProducts = {
                            onAddProducts(it)
                            showProductDialog = false
                        }
                    )
                }
                if (showDateTimePickerBegins) {
                    DateTimePicker(
                        onDateTimeSelected = { onAddDatePromoInit(it) },
                        onDismiss = { showDateTimePickerBegins = false }
                    )
                }
                if (showDateTimePickerEnds) {
                    DateTimePicker(
                        onDateTimeSelected = { onAddDatePromoEnd(it) },
                        onDismiss = { showDateTimePickerEnds = false }
                    )
                }
                Spacer(Modifier.size(16.dp))
                AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    onDateTimeSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->

                            selectedDate = Instant
                                .fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .date

                            showDatePicker = false
                            showTimePicker = true
                        }
                    },
                ) {
                    Text("Aceptar", color = GreenText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", color = GrayText)
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

    if (showTimePicker) {

        val timePickerState = rememberTimePickerState(
            initialHour = 12,
            initialMinute = 0,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = onDismiss,
            backgroundColor = PrimaryCardBackground,
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate?.let { date ->

                            val dateTime = LocalDateTime(
                                date = date,
                                time = LocalTime(
                                    hour = timePickerState.hour,
                                    minute = timePickerState.minute
                                )
                            )

                            onDateTimeSelected(dateTime)
                        }

                        onDismiss()
                    },
                ) {
                    Text("Aceptar", color = GreenText, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", color = GrayText, fontWeight = FontWeight.SemiBold)
                }
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = GreenText.copy(.6f),
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color.Black,
                        selectorColor = GreenText,
                        containerColor = AccentColor,
                        periodSelectorBorderColor = Yellowe,
                        periodSelectorSelectedContainerColor = GrayText,
                        periodSelectorUnselectedContainerColor = Color.Magenta,
                        periodSelectorSelectedContentColor = LightBlue,
                        periodSelectorUnselectedContentColor = Color.Blue,
                        timeSelectorSelectedContainerColor = GreenText.copy(.6f),
                        timeSelectorUnselectedContainerColor = GrayText,
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = Color.Black,
                    )
                )
            }
        )
    }
}

@Composable
private fun ProductsDialog(
    products: List<Product>,
    onDismiss: () -> Unit,
    onAddProducts: (List<Product>) -> Unit,
) {

    val productsSelected = remember { mutableStateListOf<Product>() }
    val interactionSource = remember { MutableInteractionSource() }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SecondaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        IconButton(
                            onClick = { onDismiss() },
                            modifier = Modifier.hoverable(interactionSource).pointerHoverIcon(
                                PointerIcon.Hand
                            )
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "Close dialog",
                                tint = Color.White
                            )
                        }
                    }
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Listado de productos",
                            modifier = Modifier.padding(top = 12.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 350.dp)
                ) {
                    if (products.isNotEmpty()) {
                        items(products) { product ->
                            CheckBoxItem(
                                name = "${product.name} '${product.brand}' ${product.description}",
                                checked = productsSelected.any { product.id == it.id },
                                onClick = {
                                    if (productsSelected.any { product.id == it.id }) productsSelected.remove(
                                        product
                                    )
                                    else productsSelected.add(product)
                                },

                                )
                        }
                    } else {
                        item {
                            Text(
                                "No hay productos",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                AcceptDeclineButtons(
                    acceptText = "Aceptar",
                    declineText = "Cancelar",
                    acceptColor = GreenText,
                    onDismiss = { onDismiss() },
                    onAccept = { onAddProducts(productsSelected) }
                )
            }
        }
    }
}

@Composable
private fun CategoriesDialog(
    categories: List<ProductCategory>,
    onDismiss: () -> Unit,
    onAddCategories: (List<ProductCategory>) -> Unit,
) {

    val categoriesSelected = remember { mutableStateListOf<ProductCategory>() }
    val interactionSource = remember { MutableInteractionSource() }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SecondaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        IconButton(
                            onClick = { onDismiss() },
                            modifier = Modifier.hoverable(interactionSource).pointerHoverIcon(
                                PointerIcon.Hand
                            )
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "Close dialog",
                                tint = Color.White
                            )
                        }
                    }
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Listado de categorías",
                            modifier = Modifier.padding(top = 12.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 350.dp)
                ) {
                    if (categories.isNotEmpty()) {
                        items(categories) { category ->
                            CheckBoxItem(
                                name = category.name,
                                checked = categoriesSelected.any { category.id == it.id },
                                onClick = {
                                    if (categoriesSelected.any { category.id == it.id }) categoriesSelected.remove(
                                        category
                                    )
                                    else categoriesSelected.add(category)
                                },

                                )
                        }
                    } else {
                        item {
                            Text(
                                "No hay categorías disponibles",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                AcceptDeclineButtons(
                    acceptText = "Aceptar",
                    declineText = "Cancelar",
                    acceptColor = GreenText,
                    onDismiss = { onDismiss() },
                    onAccept = { onAddCategories(categoriesSelected) }
                )
            }
        }
    }
}
