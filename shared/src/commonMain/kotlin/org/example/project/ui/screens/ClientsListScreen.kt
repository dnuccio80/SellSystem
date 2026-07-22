package org.example.project.ui.screens

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
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButton
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SearchTextField
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.WhiteText

class ClientsListScreen : Screen {
    @Composable
    override fun Content() {
        var showAddProductDialog by rememberSaveable { mutableStateOf(false) }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButton(
                    title = "Agregar cliente",
                    description = "Listado de todos los clientes añadidos",
                    buttonText = "Agregar cliente"
                ) { showAddProductDialog = true }
            }

            if (showAddProductDialog) {
                AddClientDialog(
                    onDismiss = { showAddProductDialog = false }
                )
            }
        }
    }
}

@Composable
private fun AddClientDialog(onDismiss: () -> Unit) {
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
                        "Agregar nuevo cliente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column{
                    GenericTextField("", "Nombre") { }
                    GenericTextField("", "Apellido") { }
                    GenericTextField("", "Número de teléfono") { }
                    GenericTextField("", "Precio de compra") { }
                    GenericTextField("", "Dirección") { }
                    GenericTextField("", "Fecha de cumpleaños") { }
                    GenericTextField("", "Notas adicionales") { }
                }
                AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
            }
        }

    }
}

