package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.example.project.ui.Capitalization
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.utils.PrimaryCardBackground

class PendingOrdersScreen: Screen {
    @Composable
    override fun Content() {
        var showNewPendingOrderDialog by rememberSaveable { mutableStateOf(false) }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Órdenes pendientes",
                    description = "Listado de órdenes para despachar",
                    buttonText = "Nueva orden pendiente"
                ) { showNewPendingOrderDialog = true }
            }

            if (showNewPendingOrderDialog) {
                AddNewPendingOrderDialog(
                    onDismiss = { showNewPendingOrderDialog = false }
                )
            }
        }
    }
}

@Composable
private fun AddNewPendingOrderDialog(onDismiss: () -> Unit) {

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
                        "Agregar nuevo proveedor",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column{
                    GenericTextField("", "Nombre del cliente", capitalizationMethod = Capitalization.WORDS) { }
                    GenericTextField("", "Teléfono", capitalizationMethod = Capitalization.NONE) { }
                    GenericTextField("", "Dirección", capitalizationMethod = Capitalization.SENTENCES) { }
                    GenericTextField("", "Estado de pedido", capitalizationMethod = Capitalization.SENTENCES) { }
                }
                AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
            }
        }
    }
}