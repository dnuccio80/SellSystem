package org.example.project.ui.screens.productvariants

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.CardTitleBackground
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.PrimaryCardBackground
import org.koin.compose.viewmodel.koinViewModel

class ProductVariantsScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<ProductVariantsViewModel>()
        val variantList by viewModel.variants.collectAsStateWithLifecycle()
        val variantTitle by viewModel.variantTitle.collectAsStateWithLifecycle()

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
                        buttonText = "Agregar variante"
                    ) { showManageVariantsDialog = true }
                }

                if (showManageVariantsDialog) {
                    AddProductDialog(
                        variantList,
                        variantTitle,
                        onTitleChange = { viewModel.updateTitle(it) },
                        onVariantChange = { index, value ->
                            viewModel.updateValue(index, value)
                        },
                        onVariantAdd = { viewModel.addField() },
                        onVariantDelete = { viewModel.removeVariant(it) },
                        onDismiss = {
                            showManageVariantsDialog = false
                            viewModel.cleanData()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AddProductDialog(
    variantList: List<String>,
    titleValue: String,
    onTitleChange: (String) -> Unit,
    onVariantChange: (Int, String) -> Unit,
    onVariantAdd: () -> Unit,
    onVariantDelete: (Int) -> Unit,
    onDismiss: () -> Unit,
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
                        "Agregar nueva variante de producto",
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
                    AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
                }

            }
        }

    }
}