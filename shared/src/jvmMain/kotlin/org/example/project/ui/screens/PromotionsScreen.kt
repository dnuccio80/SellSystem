package org.example.project.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
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
import org.example.project.ui.models.PromotionPresentation
import org.example.project.ui.screens.promotions.PromotionsViewModel
import org.example.project.ui.screens.promotions.UpdatePromotionAction
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.koin.compose.viewmodel.koinViewModel


class PromotionsScreen : Screen {
    @Composable
    override fun Content() {
        var showNewPromotionDialog by rememberSaveable { mutableStateOf(false) }
        val viewModel = koinViewModel<PromotionsViewModel>()
        val promotionData by viewModel.promotionData.collectAsStateWithLifecycle()
        val categories by viewModel.categories.collectAsStateWithLifecycle()

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
                    promotionData,
                    categories,
                    onDismiss = { showNewPromotionDialog = false },
                    onActionDone = { action, value ->
                        viewModel.updatePromotionData(action, value)
                    },
                    onAddCategories = { list ->
                        viewModel.updatePromotionListSelected(list)
                    }
                )
            }
        }
    }
}

@Composable
private fun NewPromotionDialog(
    promotionData: PromotionPresentation,
    categories: List<ProductCategory>,
    onActionDone: (UpdatePromotionAction, String) -> Unit,
    onAddCategories: (List<ProductCategory>) -> Unit,
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
                                GenericButton(
                                    text = "Seleccionar productos",
                                    icon = Icons.Outlined.KeyboardDoubleArrowRight,
                                    color = GreenText,
                                    onClick = { }
                                )
                                AnimatedContent(promotionData.categoryListSelected) {
                                    if (promotionData.categoryListSelected != null) {
                                        Text("Productos seleccionados:")
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
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            GenericButton(
                                text = "Seleccionar fecha y hora",
                                onClick = { }
                            )
                            Text(
                                "No se ha seleccionado fecha y hora",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AccentColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
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
                Spacer(Modifier.size(16.dp))
                AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
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
