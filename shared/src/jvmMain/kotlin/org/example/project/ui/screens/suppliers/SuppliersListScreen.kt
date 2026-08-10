package org.example.project.ui.screens.suppliers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import org.example.project.domain.models.supplier.Supplier
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.RowWithMidTitleAndDescription
import org.example.project.ui.ScreenContainer
import org.example.project.ui.models.SupplierPresentation
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.screens.suppliers.SupplierAction.ACCEPT
import org.example.project.ui.screens.suppliers.SupplierAction.ADDRESS
import org.example.project.ui.screens.suppliers.SupplierAction.DELETE_CONFIRM
import org.example.project.ui.screens.suppliers.SupplierAction.DISMISS
import org.example.project.ui.screens.suppliers.SupplierAction.MAIL
import org.example.project.ui.screens.suppliers.SupplierAction.NAME
import org.example.project.ui.screens.suppliers.SupplierAction.PHONE
import org.example.project.ui.screens.suppliers.SupplierAction.PRODUCTS
import org.example.project.ui.screens.suppliers.SupplierAction.WEBPAGE
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel


class SuppliersListScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<SuppliersViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var showNewSupplierDialog by rememberSaveable { mutableStateOf(false) }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
        var isEdit by rememberSaveable { mutableStateOf(false) }
        var adviceMessage by rememberSaveable { mutableStateOf("") }

        LaunchedEffect(viewModel.events) {
            viewModel.events.collect { msg ->
                adviceMessage = msg
                showAdviceDialog = true
            }
        }

        when (uiState) {
            is SuppliersUiState.Error -> {}
            SuppliersUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is SuppliersUiState.Success -> {
                ScreenContainer {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GenericHeaderWithButtonAndSearch(
                            title = "Proveedores",
                            description = "Listado de proveedores",
                            buttonText = "Nuevo proveedor"
                        ) {
                            isEdit = false
                            showNewSupplierDialog = true
                        }
                    }
                    if ((uiState as SuppliersUiState.Success).supplierList.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            items((uiState as SuppliersUiState.Success).supplierList) { supplier ->
                                supplierCardItem(supplier.toPresentation()) {
                                    viewModel.getSupplier(supplier.id) {
                                        isEdit = true
                                        showNewSupplierDialog = true
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            "No se encontraron proveedores",
                            style = MaterialTheme.typography.titleSmall,
                            color = WhiteText,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }


                }
                if (showNewSupplierDialog) {
                    AddNewSupplierDialog(
                        (uiState as SuppliersUiState.Success).supplierData,
                        isEdit = isEdit
                    ) { action, value ->
                        if (action == DISMISS) {
                            showNewSupplierDialog = false
                            isEdit = false
                            viewModel.cleanSupplierData()
                        }
                        if (action == ACCEPT) viewModel.tryAddSupplier {
                            showNewSupplierDialog = false
                        }
                        if (action == DELETE_CONFIRM) {
                            showConfirmDialog = true
                        }
                        viewModel.updateSupplierData(action, value)
                    }
                }
                SimpleAdviceDialog(
                    msg = adviceMessage,
                    show = showAdviceDialog,
                    onDismiss = { showAdviceDialog = false }
                )
                if(showConfirmDialog) {
                    ConfirmDialog(
                        msg = "Seguro que deseas eliminar el proveedor?",
                        onAccept = {
                            viewModel.deleteSupplier()
                            showConfirmDialog = false
                            isEdit = false
                            showNewSupplierDialog = false
                        },
                        onDismiss = { showConfirmDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun AddNewSupplierDialog(
    supplierData: Supplier,
    isEdit: Boolean,
    onActionDone: (SupplierAction, String) -> Unit,
) {
    Dialog(onDismissRequest = { onActionDone(DISMISS, "") }) {

        val phone = if (supplierData.phoneNumber == 0L) "" else supplierData.phoneNumber.toString()

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Agregar nuevo proveedor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    if (isEdit) {
                        GenericButton(
                            text = "Eliminar",
                            color = AccentColor,
                            onClick = { onActionDone(DELETE_CONFIRM, "") }
                        )
                    }
                }

                Column {
                    GenericTextField(
                        supplierData.name,
                        "Nombre",
                        capitalizationMethod = Capitalization.WORDS
                    ) { onActionDone(NAME, it) }
                    GenericTextField(
                        supplierData.mail,
                        "Mail",
                        capitalizationMethod = Capitalization.NONE
                    ) { onActionDone(MAIL, it) }
                    GenericTextField(
                        phone,
                        "Número de teléfono",
                        onlyNumbers = true
                    ) { onActionDone(PHONE, it) }
                    GenericTextField(
                        supplierData.address,
                        "Dirección",
                        capitalizationMethod = Capitalization.WORDS
                    ) { onActionDone(ADDRESS, it) }
                    GenericTextField(
                        supplierData.webpage,
                        "Página web",
                        capitalizationMethod = Capitalization.NONE
                    ) { onActionDone(WEBPAGE, it) }
                    GenericTextField(
                        supplierData.productsOffered,
                        "Productos que provee (separarlos por coma)"
                    ) { onActionDone(PRODUCTS, it) }
                }
                AcceptDeclineButtons(
                    onAccept = { onActionDone(ACCEPT, "") },
                    onDismiss = { onActionDone(DISMISS, "") })
            }
        }
    }
}

@Composable
fun supplierCardItem(supplier: SupplierPresentation, onClick: () -> Unit) {

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
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RowWithMidTitleAndDescription("Nombre del proveedor:", supplier.name)
            if (supplier.phoneNumber != 0L) RowWithMidTitleAndDescription(
                "Teléfono:",
                supplier.phoneNumber.toString()
            )
            if (supplier.mail.isNotBlank()) RowWithMidTitleAndDescription("Mail:", supplier.mail)
            if (supplier.address.isNotBlank()) RowWithMidTitleAndDescription(
                "Dirección:",
                supplier.address
            )
            if (supplier.webpage.isNotBlank()) RowWithMidTitleAndDescription(
                "Página web:",
                supplier.webpage
            )
            if (supplier.productsOffered.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Productos:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
                    supplier.productsOffered.forEach { product ->
                        Card(shape = RoundedCornerShape(4.dp), colors = CardDefaults.cardColors(containerColor = GreenText)) {
                            Text(
                                product,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}