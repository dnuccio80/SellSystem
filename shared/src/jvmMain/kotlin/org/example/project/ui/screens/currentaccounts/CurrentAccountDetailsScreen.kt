package org.example.project.ui.screens.currentaccounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.data.db.entities.TransactionType
import org.example.project.domain.models.currentaccount.CurrentAccountTransaction
import org.example.project.domain.usecases.currentaccounts.CurrentAccountDetailsUiState
import org.example.project.domain.usecases.currentaccounts.CurrentAccountDetailsUiState.Success
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization
import org.example.project.ui.Capitalization.NONE
import org.example.project.ui.Capitalization.SENTENCES
import org.example.project.ui.Capitalization.WORDS
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericScreenTitleHeaderWithButtons
import org.example.project.ui.GenericSelectableTextField
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SummaryCardHeader
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

class CurrentAccountDetailsScreen(val clientId: Int) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<CurrentAccountDetailsViewModel>()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        var isEdit by rememberSaveable { mutableStateOf(false) }
        var adviceMsg by rememberSaveable { mutableStateOf("") }
        var showTransactionDialog by rememberSaveable { mutableStateOf(false) }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var showConfirmDialog by rememberSaveable { mutableStateOf(false) }


        LaunchedEffect(clientId) {
            if (clientId != 0) {
                viewModel.loadCurrentAccount(clientId)
            }
        }

        LaunchedEffect(viewModel.events) {
            viewModel.events.collect { msg ->
                adviceMsg = msg
                showAdviceDialog = true
            }
        }

        when (uiState) {
            is CurrentAccountDetailsUiState.Error -> {}
            CurrentAccountDetailsUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is Success -> {
                ScreenContainer {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GenericScreenTitleHeaderWithButtons(
                            mainTitle = (uiState as Success).client.fullName,
                            description = "Detalles de cuenta corriente",
                            firstButtonText = "Agregar transacción",
                            secondButtonText = "Eliminar cuenta",
                            secondButtonColor = AccentColor,
                            onFirstButtonClick = {
                                isEdit = false
                                viewModel.cleanTransactionData()
                                showTransactionDialog = true
                            },
                            onSecondButtonClick = { },
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CurrentAccountSummaryCard(
                                icon = Icons.Outlined.AccountBalance,
                                title = "Deuda",
                                description = "Balance entre las compras y los pagos realizados por el cliente",
                                amount = (uiState as Success).balance.balance,
                                modifier = Modifier.weight(1f)
                            )
                            CurrentAccountSummaryCard(
                                icon = Icons.Outlined.ShoppingCart,
                                title = "Compras",
                                description = "Todas las compras realizadas con cuenta corriente",
                                amount = (uiState as Success).balance.purchasesAmount,
                                modifier = Modifier.weight(1f)
                            )
                            CurrentAccountSummaryCard(
                                icon = Icons.Outlined.Money,
                                title = "Pagos",
                                description = "Pagos en efectivo y transferencia de cuenta corriente",
                                amount = (uiState as Success).balance.paymentsAmount,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        TransactionsListCardItem(
                            (uiState as Success).purchasesList,
                            (uiState as Success).paymentsList,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.updateTransaction(it)
                                isEdit = true
                                showTransactionDialog = true
                            }
                        )
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            GenericButton(
                                text = "Volver",
                                color = GrayText,
                                onClick = { navigator?.pop() }
                            )
                        }
                    }
                    if (showTransactionDialog) {
                        TransactionDialog(
                            transactionData = (uiState as Success).transactionData,
                            isEdit = isEdit,
                            onDismiss = { showTransactionDialog = false },
                            onDelete = { showConfirmDialog = true },
                            onAccept = {
                                viewModel.tryAddTransaction {
                                    showTransactionDialog = false
                                }
                            },
                            onDescriptionChange = { viewModel.updateDescriptionTransactionData(it) },
                            onAmountChange = { viewModel.updateAmountTransactionData(it) },
                            onTransactionTypeChange = { viewModel.updateTransactionType(it) }
                        )
                    }
                    SimpleAdviceDialog(
                        msg = adviceMsg,
                        show = showAdviceDialog,
                        onDismiss = { showAdviceDialog = false }
                    )
                    if (showConfirmDialog) {
                        ConfirmDialog(
                            msg = "Seguro que deseas eliminar la transacción?",
                            onAccept = {
                                viewModel.deleteTransaction()
                                showConfirmDialog = false
                                showTransactionDialog = false
                            },
                            onDismiss = { showConfirmDialog = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionsListCardItem(
    purchasesList: List<CurrentAccountTransaction>,
    paymentsList: List<CurrentAccountTransaction>,
    modifier: Modifier,
    onClick: (CurrentAccountTransaction) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Compras",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                if (purchasesList.isNotEmpty()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(purchasesList) { transaction ->
                            TransactionCardItem(transaction, Modifier.weight(1f)) {
                                onClick(
                                    transaction
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        "No hay elementos disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        color = WhiteText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            VerticalDivider(
                thickness = 2.dp,
                color = GrayText,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Pagos",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                if (paymentsList.isNotEmpty()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(paymentsList) { transaction ->
                            TransactionCardItem(transaction, Modifier.weight(1f)) {
                                onClick(
                                    transaction
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        "No hay elementos disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        color = WhiteText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionCardItem(
    transaction: CurrentAccountTransaction,
    modifier: Modifier,
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val cardColor = if (isHovered) GrayText else SecondaryCardBackground

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier.pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource).clickable { onClick() }
    ) {
        Row(
            Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                transaction.description,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                transaction.amount.toPrice(),
                color = GreenText,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun TransactionDialog(
    transactionData: CurrentAccountTransaction,
    isEdit: Boolean,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onAccept: () -> Unit,
    onTransactionTypeChange: (TransactionType) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
) {

    val transactionType = listOf(
        TransactionType.PURCHASE,
        TransactionType.PAYMENT
    )

    var showDropdownMenuCategory by rememberSaveable { mutableStateOf(false) }
    var transactionTypeSelected by rememberSaveable { mutableStateOf("") }

    val amount = if (transactionData.amount == 0L) "" else transactionData.amount.toString()

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
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Nueva transacción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    if (isEdit) {
                        GenericButton(
                            text = "Eliminar",
                            color = AccentColor,
                            onClick = { onDelete() }
                        )
                    }
                }
                Column {
                    GenericSelectableTextField(
                        value = transactionData.type.etiquette,
                        labelText = "Tipo de transacción",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showDropdownMenuCategory = true }
                    )
                    DropdownMenu(
                        expanded = showDropdownMenuCategory,
                        onDismissRequest = { showDropdownMenuCategory = false },
                        modifier = Modifier.background(SecondaryCardBackground).width(350.dp)
                    ) {
                        transactionType.forEach { transaction ->
                            DropdownMenuItem(
                                text = { Text(transaction.etiquette) },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                                onClick = {
                                    onTransactionTypeChange(transaction)
                                    showDropdownMenuCategory = false
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
                GenericTextField(
                    transactionData.description,
                    "Descripción",
                    capitalizationMethod = SENTENCES
                ) { onDescriptionChange(it) }
                GenericTextField(
                    amount,
                    "Monto",
                    onlyNumbers = true,
                    isPrice = true,
                    capitalizationMethod = NONE
                ) { onAmountChange(it) }
                Spacer(Modifier.size(0.dp))
                AcceptDeclineButtons(
                    acceptColor = GreenText,
                    onAccept = { onAccept() },
                    onDismiss = { onDismiss() })
            }
        }
    }
}

@Composable
private fun CurrentAccountSummaryCard(
    icon: ImageVector,
    title: String,
    description: String,
    amount: Long,
    modifier: Modifier,
) {

    val amountColor = when {
        amount == 0L -> WhiteText
        amount > 0L -> GreenText
        else -> GreenText
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(icon, title, description)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    amount.absoluteValue.toPrice(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = amountColor
                )
                if(amount < 0) {
                    Card(shape = RoundedCornerShape(4.dp), colors = CardDefaults.cardColors(containerColor = GreenText), elevation = CardDefaults.cardElevation(4.dp)) {
                        Text("Saldo a favor del cliente", color = Color.White, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(4.dp))
                    }
                }
            }

        }
    }
}