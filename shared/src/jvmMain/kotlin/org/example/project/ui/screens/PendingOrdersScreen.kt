package org.example.project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
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
import org.example.project.domain.models.pendingorders.PendingOrder
import org.example.project.domain.models.pendingorders.PendingOrderPriority.*
import org.example.project.domain.models.pendingorders.PendingOrderState.*
import org.example.project.domain.usecases.pendingorders.PendingOrderTarget
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization
import org.example.project.ui.Capitalization.SENTENCES
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericSelectableTextField
import org.example.project.ui.GenericTextField
import org.example.project.ui.RadioButtonRowWithText
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SearchTextField
import org.example.project.ui.TextArea
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.screens.pendingorders.PendingOrdersViewModel
import org.example.project.ui.screens.pendingorders.UpdatePendingOrderAction
import org.example.project.ui.screens.pendingorders.UpdatePendingOrderAction.*
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.LightBlue
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.example.project.ui.utils.Yellowe
import org.koin.compose.viewmodel.koinViewModel


class PendingOrdersScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<PendingOrdersViewModel>()

        val query by viewModel.query.collectAsStateWithLifecycle()
        val pendingOrderList by viewModel.pendingOrders.collectAsStateWithLifecycle()
        val pendingOrderData by viewModel.newPendingOrderData.collectAsStateWithLifecycle()
        var adviceMsg by rememberSaveable { mutableStateOf("") }
        var showAdviceMsg by rememberSaveable { mutableStateOf(false) }
        var showNewPendingOrderDialog by rememberSaveable { mutableStateOf(false) }
        var isEdit by rememberSaveable { mutableStateOf(false) }
        val watchFilterSelected by viewModel.watchFilterSelected.collectAsStateWithLifecycle()

        val watchFilter = listOf(
            PendingOrderTarget.ACTIVE,
            PendingOrderTarget.DONE
        )

        LaunchedEffect(viewModel.events) {
            viewModel.events.collect { msg ->
                adviceMsg = msg
                showAdviceMsg = true
            }
        }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Header(
                    searchValue = query,
                    watchFilter = watchFilter,
                    watchFilterSelected = watchFilterSelected,
                    onDeleteQuerySearch = { viewModel.updateQuery("") },
                    onSearchValueChange = { viewModel.updateQuery(it) },
                    onButtonClick = {
                        isEdit = false
                        showNewPendingOrderDialog = true
                    },
                    onTargetChange = { viewModel.updatePendingOrderTarget(it) }
                )
                if (pendingOrderList.isNotEmpty()) {
                    LazyVerticalGrid(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        columns = GridCells.Adaptive(180.dp),
                    ) {
                        items(pendingOrderList) { pendingOrder ->
                            PendingOrderSheet(pendingOrder) {
                                viewModel.getPendingOrderAndUpdatePendingOrderDataById(pendingOrder.id)
                                isEdit = true
                                showNewPendingOrderDialog = true
                            }
                        }
                    }
                } else {
                    Text(
                        "No hay órdenes pendientes en la búsqueda",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (showNewPendingOrderDialog) {
                AddNewPendingOrderDialog(
                    pendingOrderData,
                    isEdit,
                    onActionDone = { action, value ->
                        viewModel.updatePendingOrderData(value, action)
                    },
                    onAccept = {
                        viewModel.tryAddPendingOrder {
                            showNewPendingOrderDialog = false
                        }
                    },
                    onDismiss = {
                        viewModel.cleanPendingOrderData()
                        showNewPendingOrderDialog = false
                    }
                )
            }
            SimpleAdviceDialog(
                msg = adviceMsg,
                show = showAdviceMsg,
                onDismiss = { showAdviceMsg = false })
        }
    }
}

@Composable
private fun Header(
    searchValue: String,
    watchFilter: List<PendingOrderTarget>,
    watchFilterSelected: PendingOrderTarget,
    onDeleteQuerySearch: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onTargetChange: (PendingOrderTarget) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    "Órdenes pendientes",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    "Listado de órdenes para despachar",
                    color = WhiteText,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            SearchTextField(
                searchValue,
                placeHolderText = "Buscar por cliente..",
                onDelete = { onDeleteQuerySearch() },
                onValueChange = { onSearchValueChange(it) },
                capitalization = SENTENCES
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Ver:",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            watchFilter.forEach { target ->
                RadioButtonRowWithText(
                    name = target.etiquette,
                    selected = watchFilterSelected.etiquette,
                    onClick = { onTargetChange(target) }
                )
            }
        }
        GenericButton(
            text = "Nueva orden pendiente",
        ) {
            onButtonClick()
        }
    }
}


@Composable
private fun PendingOrderSheet(order: PendingOrder, onClick: () -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val normalColor = when (order.priority) {
        HIGH -> AccentColor
        MID -> Yellowe
        LOW -> LightBlue
    }

    val color = if (isHovered) normalColor.copy(alpha = .8f) else normalColor

    Card(
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier.size(180.dp).hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand).clickable { onClick() }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Para: ${order.clientName}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                "Estado: ${order.status.etiquette}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AddNewPendingOrderDialog(
    data: PendingOrder,
    isEdit: Boolean,
    onActionDone: (UpdatePendingOrderAction, String) -> Unit,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
) {

    val pendingOrderState = listOf(
        PENDING,
        CONFIRMED,
        PREPARING,
        SENT,
        CANCELED,
        COMPLETED,
    )

    val pendingOrderPriority = listOf(
        HIGH,
        MID,
        LOW,
    )
    val title = if (isEdit) "Modificar orden pendiente" else "Agregar nueva orden pendiente"
    val phone = if (data.phone == 0L) "" else data.phone.toString()

    var showDropdownMenuPendingOrderState by rememberSaveable { mutableStateOf(false) }
    var showDropdownMenuPendingOrderPriority by rememberSaveable { mutableStateOf(false) }

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
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column {
                    GenericTextField(
                        data.clientName,
                        "Nombre del cliente",
                        capitalizationMethod = Capitalization.WORDS
                    ) { onActionDone(CLIENT, it) }
                    GenericTextField(
                        phone,
                        "Teléfono",
                        onlyNumbers = true,
                        capitalizationMethod = Capitalization.NONE
                    ) { onActionDone(PHONE, it) }
                    GenericTextField(
                        data.address,
                        "Dirección",
                        capitalizationMethod = Capitalization.SENTENCES
                    ) { onActionDone(ADDRESS, it) }
                    TextArea(value = data.description, onValueChange = {
                        onActionDone(
                            DESCRIPTION, it
                        )
                    })
                    Column {
                        GenericSelectableTextField(
                            value = data.status.etiquette,
                            labelText = "Estado de pedido",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showDropdownMenuPendingOrderState = true
                            }
                        )
                        DropdownMenu(
                            expanded = showDropdownMenuPendingOrderState,
                            onDismissRequest = { showDropdownMenuPendingOrderState = false },
                            modifier = Modifier.background(SecondaryCardBackground).width(350.dp),
                            scrollState = rememberScrollState()
                        ) {
                            pendingOrderState.forEach { state ->
                                DropdownMenuItem(
                                    text = { Text(state.etiquette) },
                                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                    onClick = {
                                        showDropdownMenuPendingOrderState = false
                                        onActionDone(STATUS, state.etiquette)
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
                        }
                    }
                    Column {
                        GenericSelectableTextField(
                            value = data.priority.etiquette,
                            labelText = "Prioridad",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { showDropdownMenuPendingOrderPriority = true }
                        )
                        DropdownMenu(
                            expanded = showDropdownMenuPendingOrderPriority,
                            onDismissRequest = { showDropdownMenuPendingOrderPriority = false },
                            modifier = Modifier.background(SecondaryCardBackground).width(350.dp),
                            scrollState = rememberScrollState()
                        ) {
                            pendingOrderPriority.forEach { priority ->
                                DropdownMenuItem(
                                    text = { Text(priority.etiquette) },
                                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                    onClick = {
                                        showDropdownMenuPendingOrderPriority = false
                                        onActionDone(PRIORITY, priority.etiquette)
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
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    AcceptDeclineButtons(onAccept = { onAccept() }, onDismiss = { onDismiss() })
                }
            }
        }
    }
}