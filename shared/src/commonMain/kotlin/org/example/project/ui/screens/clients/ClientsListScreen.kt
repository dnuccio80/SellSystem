package org.example.project.ui.screens.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.domain.models.Client
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization.NONE
import org.example.project.ui.Capitalization.SENTENCES
import org.example.project.ui.Capitalization.WORDS
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.screens.clients.ClientValueChangeActions.ADDRESS
import org.example.project.ui.screens.clients.ClientValueChangeActions.BIRTHDAY
import org.example.project.ui.screens.clients.ClientValueChangeActions.NAME
import org.example.project.ui.screens.clients.ClientValueChangeActions.NOTES
import org.example.project.ui.screens.clients.ClientValueChangeActions.PHONE_NUMBER
import org.example.project.ui.utils.CardTitleBackground
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.koin.compose.viewmodel.koinViewModel

class ClientsListScreen : Screen {
    @Composable
    override fun Content() {

        val viewmodel = koinViewModel<ClientsViewModel>()
        var showAddProductDialog by rememberSaveable { mutableStateOf(false) }
        val clientData by viewmodel.clientData.collectAsStateWithLifecycle()
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var message by rememberSaveable { mutableStateOf("") }

        val clientList by viewmodel.clientList.collectAsStateWithLifecycle()

        LaunchedEffect(viewmodel.events) {
            viewmodel.events.collect { msg ->
                message = msg
                showAdviceDialog = true
            }
        }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Agregar cliente",
                    description = "Listado de todos los clientes añadidos",
                    buttonText = "Agregar cliente"
                ) { showAddProductDialog = true }
                Spacer(modifier = Modifier.size(32.dp))
                if (clientList.isEmpty()) {
                    Text(
                        "No hay clientes, proba agregar clickeando el botón de 'Agregar cliente'",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    clientList.forEach { client ->
                        ClientCard(client)
                    }
                }
            }

            if (showAddProductDialog) {
                AddClientDialog(
                    clientData,
                    onActionDone = { action, value ->
                        when (action) {
                            NAME -> viewmodel.updateName(value)
                            PHONE_NUMBER -> viewmodel.updatePhoneNumber(value)
                            ADDRESS -> viewmodel.updateAddress(value)
                            BIRTHDAY -> viewmodel.updateBirthDay(value)
                            NOTES -> viewmodel.updateNotes(value)
                        }
                    },
                    onAccept = { viewmodel.addClient { showAddProductDialog = false } },
                    onDismiss = {
                        showAddProductDialog = false
                        viewmodel.cleanData()
                    }
                )
            }
        }
        SimpleAdviceDialog(message, showAdviceDialog) { showAdviceDialog = false }
    }
}

@Composable
private fun ClientCard(client: Client) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = CardTitleBackground)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Text(
                client.fullName,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Tel:",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    client.phoneNumber.toString(),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Dirección:",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,

                )
                Text(
                    client.address,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Puntos de fidelidad:",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    client.loyaltyPoints.toString(),
                    fontWeight = FontWeight.SemiBold,
                    color = GreenText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

enum class ClientValueChangeActions {
    NAME, PHONE_NUMBER, ADDRESS, BIRTHDAY, NOTES
}

@Composable
fun SimpleAdviceDialog(msg: String, show: Boolean, onDismiss: () -> Unit) {

    if (!show) return
    Dialog(onDismissRequest = { }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(msg, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                GenericButton(
                    text = "Aceptar",
                    onClick = { onDismiss() }
                )
            }
        }
    }
}

@Composable
private fun AddClientDialog(
    clientData: Client,
    onActionDone: (ClientValueChangeActions, String) -> Unit,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {

    val phoneNumber = if (clientData.phoneNumber == 0L) "" else clientData.phoneNumber.toString()

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
                Column {
                    GenericTextField(
                        clientData.fullName,
                        "Nombre completo",
                        capitalizationMethod = WORDS
                    ) { onActionDone(NAME, it) }
                    GenericTextField(
                        phoneNumber,
                        "Número de teléfono",
                        onlyNumbers = true,
                        capitalizationMethod = NONE
                    ) { onActionDone(PHONE_NUMBER, it) }
                    GenericTextField(
                        clientData.address,
                        "Dirección",
                        capitalizationMethod = WORDS
                    ) { onActionDone(ADDRESS, it) }
                    GenericTextField(
                        clientData.birthday,
                        "Fecha de cumpleaños",
                        capitalizationMethod = NONE
                    ) { onActionDone(BIRTHDAY, it) }
                    GenericTextField(
                        clientData.notes,
                        "Notas adicionales",
                        capitalizationMethod = SENTENCES
                    ) { onActionDone(NOTES, it) }
                }
                Spacer(Modifier.size(0.dp))
                AcceptDeclineButtons(onAccept = { onAccept() }, onDismiss = { onDismiss() })
            }
        }

    }
}

