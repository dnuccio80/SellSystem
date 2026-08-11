package org.example.project.ui.screens.currentaccounts

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
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
import org.example.project.data.db.entities.relations.ClientWithCurrentAccount
import org.example.project.domain.models.client.Client
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericSelectableTextField
import org.example.project.ui.GenericTextField
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel

class CurrentAccountsListScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<CurrentAccountsViewModel>()
        var showNewCurrentAccountDialog by rememberSaveable { mutableStateOf(false) }
        val clients by viewModel.clients.collectAsStateWithLifecycle()
        val query by viewModel.query.collectAsStateWithLifecycle()
        val selectedClient by viewModel.selectedClient.collectAsStateWithLifecycle()
        val currentAccounts by viewModel.currentAccounts.collectAsStateWithLifecycle()

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Cuentas corrientes",
                    description = "Listado de cuentas corrientes abiertas",
                    buttonText = "Nueva cuenta corriente",
                    querySearchCapitalization = Capitalization.WORDS,
                    searchValue = query,
                    onSearchValueChange = { viewModel.updateQuery(it) },
                    onDeleteQuerySearch = { viewModel.updateQuery("") }
                ) { showNewCurrentAccountDialog = true }
                if (currentAccounts.isNotEmpty()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(currentAccounts) {clientWithCurrentAccount ->
                            CurrentAccountItem(clientWithCurrentAccount) { }
                        }
                     }
                } else {
                    Text(
                        "No hay cuentas corrientes en la búsqueda",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
        }

        if (showNewCurrentAccountDialog) {
            AddCurrentAccountDialog(
                clients = clients,
                selectedClient = selectedClient,
                onClientClick = { viewModel.updateSelectedClient(it) },
                onAccept = { viewModel.addCurrentAccount() },
                onDismiss = { showNewCurrentAccountDialog = false },
            )
        }
    }
}


@Composable
private fun AddCurrentAccountDialog(
    clients: List<Client>,
    selectedClient: Client,
    onClientClick: (Int) -> Unit,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {

    val currentTypeList = listOf(
        "Proveedor",
        "Cliente"
    )
    var typeSelected by rememberSaveable { mutableStateOf(currentTypeList.first()) }
    var showClientDropdownMenu by rememberSaveable { mutableStateOf(false) }

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
                            Column {
                                GenericSelectableTextField(
                                    value = selectedClient.fullName,
                                    labelText = "Seleccionar cliente",
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { showClientDropdownMenu = true }
                                )
                                DropdownMenu(
                                    expanded = showClientDropdownMenu,
                                    onDismissRequest = { showClientDropdownMenu = false },
                                    modifier = Modifier.background(SecondaryCardBackground)
                                        .width(350.dp).heightIn(max = 200.dp),
                                    scrollState = rememberScrollState()
                                ) {
                                    if (clients.isNotEmpty()) {
                                        clients.forEach { client ->
                                            DropdownMenuItem(
                                                text = { Text(client.fullName) },
                                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                                onClick = {
                                                    onClientClick(client.id)
                                                    showClientDropdownMenu = false
                                                },
                                                colors = MenuItemColors(
                                                    textColor = Color.White,
                                                    leadingIconColor = Color.White,
                                                    trailingIconColor = Color.White,
                                                    disabledTextColor = Color.White,
                                                    disabledLeadingIconColor = Color.White,
                                                    disabledTrailingIconColor = Color.White,
                                                )
                                            )
                                        }
                                    } else {
                                        DropdownMenuItem(
                                            text = { Text("No hay clientes disponibles") },
                                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                            onClick = { },
                                            colors = MenuItemColors(
                                                textColor = Color.White,
                                                leadingIconColor = Color.White,
                                                trailingIconColor = Color.White,
                                                disabledTextColor = Color.White,
                                                disabledLeadingIconColor = Color.White,
                                                disabledTrailingIconColor = Color.White,
                                            )
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
                AcceptDeclineButtons(onAccept = { onAccept() }, onDismiss = { onDismiss() })
            }
        }
    }
}

@Composable
private fun CurrentAccountItem(
    clientWithCurrentAccount: ClientWithCurrentAccount,
    onClick: () -> Unit,
) {

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
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                clientWithCurrentAccount.client.fullName,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Deuda:",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    clientWithCurrentAccount.currentAccount!!.amount.toPrice(),
                    fontWeight = FontWeight.Bold,
                    color = GreenText,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }
    }
}
