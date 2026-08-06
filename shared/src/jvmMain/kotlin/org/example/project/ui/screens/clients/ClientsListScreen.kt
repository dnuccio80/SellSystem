package org.example.project.ui.screens.clients

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.domain.models.client.Client
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization.NONE
import org.example.project.ui.Capitalization.SENTENCES
import org.example.project.ui.Capitalization.WORDS
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericSelectableTextField
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.formatToDisplay
import org.example.project.ui.screens.addproducts.DatePickerDialogItem
import org.example.project.ui.screens.clients.ClientValueChangeActions.ADDRESS
import org.example.project.ui.screens.clients.ClientValueChangeActions.CITY
import org.example.project.ui.screens.clients.ClientValueChangeActions.NAME
import org.example.project.ui.screens.clients.ClientValueChangeActions.NOTES
import org.example.project.ui.screens.clients.ClientValueChangeActions.PHONE_NUMBER
import org.example.project.ui.screens.clients.ClientValueChangeActions.PROVINCE
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant

class ClientsListScreen : Screen {
    @Composable
    override fun Content() {

        val viewmodel = koinViewModel<ClientsViewModel>()
        var showAddClientDialog by rememberSaveable { mutableStateOf(false) }
        val clientData by viewmodel.clientData.collectAsStateWithLifecycle()
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var message by rememberSaveable { mutableStateOf("") }
        var isModification by rememberSaveable { mutableStateOf(false) }

        val clientList by viewmodel.clientList.collectAsStateWithLifecycle()
        val querySearch by viewmodel.queryClientName.collectAsStateWithLifecycle()

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
                    buttonText = "Agregar cliente",
                    searchValue = querySearch,
                    querySearchCapitalization = WORDS,
                    onSearchValueChange = { viewmodel.updateQuerySearch(it) },
                    onDeleteQuerySearch = { viewmodel.updateQuerySearch("") }
                ) {
                    isModification = false
                    showAddClientDialog = true
                }
                Spacer(modifier = Modifier.size(32.dp))
                if (clientList.isEmpty()) {
                    Text(
                        "No hay clientes, proba agregar clickeando el botón de 'Agregar cliente'",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    clientList.forEach { client ->
                        ClientCard(client) {
                            viewmodel.getClientData(client.id) {
                                isModification = true
                                showAddClientDialog = true
                            }
                        }
                    }
                }
            }

            if (showAddClientDialog) {
                AddClientDialog(
                    clientData,
                    onActionDone = { action, value ->
                        when (action) {
                            NAME -> viewmodel.updateName(value)
                            PHONE_NUMBER -> viewmodel.updatePhoneNumber(value)
                            ADDRESS -> viewmodel.updateAddress(value)
                            NOTES -> viewmodel.updateNotes(value)
                            CITY -> viewmodel.updateCity(value)
                            PROVINCE -> viewmodel.updateProvince(value)
                        }
                    },
                    isModification = isModification,
                    onUpdateBirthday = { viewmodel.updateBirthDay(it) },
                    onAccept = { viewmodel.addClient { showAddClientDialog = false } },
                    onDismiss = {
                        showAddClientDialog = false
                        viewmodel.cleanData()
                    }
                )
            }
        }
        SimpleAdviceDialog(message, showAdviceDialog) { showAdviceDialog = false }
    }
}

@Composable
private fun ClientCard(client: Client, onClick: () -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val cardColor = if (isHovered) SecondaryCardBackground else PrimaryCardBackground


    Card(
        modifier = Modifier.fillMaxWidth()
            .pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
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
                    fontWeight = FontWeight.Bold,
                    color = GreenText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

enum class ClientValueChangeActions {
    NAME, PHONE_NUMBER, ADDRESS, CITY, PROVINCE, NOTES
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
fun ConfirmDialog(msg:String, onAccept: () -> Unit , onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
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
                AcceptDeclineButtons(
                    onDismiss = { onDismiss() },
                    onAccept = { onAccept() }
                )
            }
        }
    }
}

@Composable
private fun AddClientDialog(
    clientData: Client,
    onActionDone: (ClientValueChangeActions, String) -> Unit,
    onUpdateBirthday:(LocalDate) -> Unit,
    isModification: Boolean,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {

    val phoneNumber = if (clientData.phoneNumber == 0L) "" else clientData.phoneNumber.toString()
    val acceptText = if (isModification) "Modificar" else "Aceptar"

    val datePickerState = rememberDatePickerState()
    var showBirthdayPicker by rememberSaveable { mutableStateOf(false) }

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
                        clientData.city,
                        "Ciudad",
                        capitalizationMethod = WORDS
                    ) { onActionDone(CITY, it) }
                    GenericTextField(
                        clientData.province,
                        "Provincia",
                        capitalizationMethod = WORDS
                    ) { onActionDone(PROVINCE, it) }
                    GenericSelectableTextField(
                        value = clientData.birthday?.formatToDisplay() ?: "" ,
                        labelText = "Fecha de cumpleaños",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showBirthdayPicker = true }
                    )

                    GenericTextField(
                        clientData.notes,
                        "Notas adicionales",
                        capitalizationMethod = SENTENCES
                    ) { onActionDone(NOTES, it) }
                }
                Spacer(Modifier.size(0.dp))
                AcceptDeclineButtons(
                    acceptText = acceptText,
                    acceptColor = GreenText,
                    onAccept = { onAccept() },
                    onDismiss = { onDismiss() })
                DatePickerDialogItem(
                    show = showBirthdayPicker,
                    onDismiss = { showBirthdayPicker = false },
                    onConfirm = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC)
                                .date
                            onUpdateBirthday(date)
                            showBirthdayPicker = false
                        }
                    },
                    datePickerState = datePickerState
                )
            }
        }

    }
}

