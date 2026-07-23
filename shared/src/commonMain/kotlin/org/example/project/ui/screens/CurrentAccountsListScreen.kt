package org.example.project.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.ScreenContainer
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground

class CurrentAccountsListScreen : Screen {
    @Composable
    override fun Content() {
        var showNewCurrentAccountDialog by rememberSaveable { mutableStateOf(false) }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Cuentas corrientes",
                    description = "Listado de cuentas corrientes abiertas",
                    buttonText = "Nueva cuenta corriente"
                ) { showNewCurrentAccountDialog = true }
            }

            if (showNewCurrentAccountDialog) {
                AddCurrentAccountDialog(
                    onDismiss = { showNewCurrentAccountDialog = false }
                )
            }
        }
    }
}


@Composable
private fun AddCurrentAccountDialog(onDismiss: () -> Unit) {

    val currentTypeList = listOf(
        "Proveedor",
        "Cliente"
    )

    var typeSelected by rememberSaveable { mutableStateOf(currentTypeList.first()) }

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
                        "Agregar nueva cuenta corriente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column {
                    currentTypeList.forEach { type ->
                        RadioButtonRowWithText(
                            name = type,
                            selected = typeSelected,
                            onClick = { typeSelected = type },
                        )
                    }
                }
                AnimatedContent(typeSelected) {
                    when {
                        typeSelected == currentTypeList.first() -> {
                            GenericTextField(
                                "",
                                labelText = "Seleccionar proveedor",
                                onValueChange = { },
                                capitalizationMethod = Capitalization.NONE
                            )
                        }
                        else -> {
                            GenericTextField(
                                "",
                                labelText = "Seleccionar cliente",
                                onValueChange = { },
                                capitalizationMethod = Capitalization.NONE
                            )
                        }
                    }
                }
                AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
            }
        }
    }
}

