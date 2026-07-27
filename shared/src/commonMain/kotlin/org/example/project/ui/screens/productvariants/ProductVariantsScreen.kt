package org.example.project.ui.screens.productvariants

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.domain.models.ProductVariant
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel

class ProductVariantsScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<ProductVariantsViewModel>()
        val variantList by viewModel.variants.collectAsStateWithLifecycle()
        val variantTitle by viewModel.variantTitle.collectAsStateWithLifecycle()
        val productVariants by viewModel.allProductVariants.collectAsStateWithLifecycle()
        val querySearch by viewModel.querySearch.collectAsStateWithLifecycle()

        var eventMsg by rememberSaveable { mutableStateOf("") }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
        var isEdit by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(viewModel.events) {
            viewModel.events.collect { msg ->
                eventMsg = msg
                showAdviceDialog = true
            }
        }

        ScreenContainer {
            var showManageVariantsDialog by rememberSaveable { mutableStateOf(false) }

            ScreenContainer {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    GenericHeaderWithButtonAndSearch(
                        title = "Variantes de productos",
                        description = "Gestión de variantes de productos como color, cantidad, etc.",
                        searchValue = querySearch,
                        onSearchValueChange = { viewModel.updateQuerySearch(it) },
                        onDeleteQuerySearch = { viewModel.updateQuerySearch("") },
                        buttonText = "Agregar variante"
                    ) {
                        isEdit = false
                        showManageVariantsDialog = true
                    }
                    Spacer(Modifier.size(16.dp))
                    if (productVariants.isNotEmpty()) {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            productVariants.forEach { productVariant ->
                                ProductVariantCard(productVariant) {
                                    isEdit = true
                                    viewModel.editProductVariant(
                                        productVariant.id
                                    ) { showManageVariantsDialog = true }
                                }
                            }
                        }
                    } else {
                        Text(
                            "No hay variantes de momento",
                            style = MaterialTheme.typography.titleMedium,
                            color = WhiteText
                        )
                    }
                }
                if (showManageVariantsDialog) {
                    ProductVariantDialog(
                        variantList,
                        variantTitle,
                        isEdit = isEdit,
                        onTitleChange = { viewModel.updateTitle(it) },
                        onVariantChange = { index, value ->
                            viewModel.updateValue(index, value)
                        },
                        onVariantAdd = { viewModel.addField() },
                        onVariantDelete = { viewModel.removeVariant(it) },
                        onAccept = {
                            viewModel.addNewProductVariant {
                                showManageVariantsDialog = false
                            }
                        },
                        onDismiss = {
                            showManageVariantsDialog = false
                            viewModel.cleanData()
                        },
                        onDelete = { showConfirmDialog = true }
                    )
                }
            }
            if (showAdviceDialog) {
                SimpleAdviceDialog(
                    msg = eventMsg,
                    show = showAdviceDialog,
                    onDismiss = { showAdviceDialog = false },
                )
            }

            if (showConfirmDialog) {
                ConfirmDialog(
                    "Seguro que deseas eliminar la variante de producto?",
                    onAccept = {
                        showConfirmDialog = false
                        showManageVariantsDialog = false
                        viewModel.deleteProductVariantById()
                    },
                    onDismiss = { showConfirmDialog = false }
                )
            }
        }
    }
}

@Composable
private fun ProductVariantCard(productVariant: ProductVariant, onClick: () -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val cardColor = if (isHovered) PrimaryCardBackground else SecondaryCardBackground

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
            .pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Tipo de variante:",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    productVariant.name,
                    fontWeight = FontWeight.Bold,
                    color = GreenText,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Variantes:",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                )
                productVariant.variants.forEach {
                    VariantsCard(it)
                }
            }
        }
    }
}

@Composable
private fun VariantsCard(variant: String) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = GreenText)
    ) {
        Text(
            variant,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun ProductVariantDialog(
    variantList: List<String>,
    titleValue: String,
    isEdit: Boolean,
    onTitleChange: (String) -> Unit,
    onVariantChange: (Int, String) -> Unit,
    onVariantAdd: () -> Unit,
    onVariantDelete: (Int) -> Unit,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    onDelete:() -> Unit
) {
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
                        "Nueva variante de producto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier.verticalScroll(
                        rememberScrollState()
                    ).weight(1f)
                ) {
                    GenericTextField(titleValue, "Nombre de la variante") { onTitleChange(it) }
                    AnimatedContent(variantList.size) {
                        Column(modifier = Modifier.fillMaxWidth().padding(start = 32.dp)) {
                            variantList.forEachIndexed { index, value ->

                                val interactionSource = remember { MutableInteractionSource() }
                                val isHovered by interactionSource.collectIsHoveredAsState()

                                val iconColor = if (isHovered) AccentColor else Color.White

                                GenericTextField(
                                    value,
                                    "Variante ${index + 1}",
                                    trailingIcon = {
                                        Icon(
                                            Icons.Outlined.Delete,
                                            contentDescription = "delete variant",
                                            modifier = Modifier.clickable { onVariantDelete(index) }
                                                .pointerHoverIcon(
                                                    PointerIcon.Hand
                                                ).hoverable(interactionSource),
                                            tint = iconColor
                                        )
                                    }) { onVariantChange(index, it) }
                            }
                        }
                    }
                    Spacer(Modifier.size(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        GenericButton(
                            "Agregar variante",
                            onClick = { onVariantAdd() }
                        )
                    }
                    Spacer(Modifier.size(16.dp))
                    Spacer(Modifier.weight(1f))
                    AcceptDeclineButtons(onAccept = { onAccept() }, onDismiss = { onDismiss() })
                    if (isEdit) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            GenericButton("Eliminar", color = AccentColor) { onDelete() }
                        }
                    }
                }

            }
        }

    }
}