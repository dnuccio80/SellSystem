package org.example.project.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.ScreenContainer
import org.example.project.ui.utils.PrimaryCardBackground

enum class PromotionType(val title: String) {
    BUY_X_PAY_Y("Compra X, paga Y"), PERCENT("Por porcentaje")
}

enum class PromotionCategory(val title: String) {
    BRAND("Marca"), PRODUCT_TYPE("Tipo de producto"), SPECIFIC("Producto específico")
}

class PromotionsScreen : Screen {
    @Composable
    override fun Content() {
        var showNewPromotionDialog by rememberSaveable { mutableStateOf(false) }

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
                    onDismiss = { showNewPromotionDialog = false }
                )
            }
        }
    }
}



@Composable
private fun NewPromotionDialog(onDismiss: () -> Unit) {

    val promotionType = listOf(
        PromotionType.BUY_X_PAY_Y,
        PromotionType.PERCENT
    )

    val promotionCategory = listOf(
        PromotionCategory.BRAND,
        PromotionCategory.PRODUCT_TYPE,
        PromotionCategory.SPECIFIC
    )

    var promoTypeSelected by rememberSaveable { mutableStateOf(promotionType.first().title) }
    var promoCategorySelected by rememberSaveable { mutableStateOf(promotionCategory.first().title) }


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
                                selected = promoTypeSelected,
                                onClick = { promoTypeSelected = promo.title },
                            )
                        }
                    }
                    AnimatedContent(promoTypeSelected) {
                        when (promoTypeSelected) {
                            PromotionType.BUY_X_PAY_Y.title -> {
                                Column {
                                    GenericTextField(
                                        value = "",
                                        labelText = "Compra",
                                        onValueChange = { }
                                    )
                                    GenericTextField(
                                        value = "",
                                        labelText = "Paga",
                                        onValueChange = { }
                                    )
                                }
                            }
                            PromotionType.PERCENT.title -> {
                                GenericTextField(
                                    value = "",
                                    labelText = "Porcentaje de descuento",
                                    onValueChange = { }
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
                                selected = promoCategorySelected,
                                onClick = { promoCategorySelected = category.title }
                            )
                        }
                    }
                    AnimatedContent(promoCategorySelected) {
                        when (promoCategorySelected) {
                            PromotionCategory.PRODUCT_TYPE.title -> {
                                GenericTextField(
                                    value = "",
                                    labelText = "Tipo de producto",
                                    onValueChange = { }
                                )
                            }

                            PromotionCategory.BRAND.title -> {
                                GenericTextField(
                                    value = "",
                                    labelText = "Marca",
                                    onValueChange = { }
                                )
                            }

                            else -> {
                                GenericTextField(
                                    value = "",
                                    labelText = "Seleccionar producto",
                                    onValueChange = { }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.size(16.dp))
                AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
            }
        }

    }
}
