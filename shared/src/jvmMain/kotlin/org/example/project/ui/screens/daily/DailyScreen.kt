package org.example.project.ui.screens.daily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.domain.models.daily.DailyData
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericScreenTitleHeaderWithButtons
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SummaryCardHeader
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.screens.daily.DailyDataAction.UPDATE_CASH
import org.example.project.ui.screens.daily.DailyDataAction.UPDATE_VIRTUAL_ACCOUNT
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel

class DailyScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<DailyViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        var confirmMsg by rememberSaveable { mutableStateOf("") }
        var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
        var adviceMsg by rememberSaveable { mutableStateOf("") }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }

        var showOpenFinanceDialog by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(viewModel.events) {
            viewModel.events.collect { msg ->
                adviceMsg = msg
                showAdviceDialog = true
            }
        }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericScreenTitleHeaderWithButtons(
                    mainTitle = "Caja diaria",
                    description = "Movimientos de dinero del día",
                    firstButtonText = if (!uiState.isOpen) "Abrir caja" else "Caja diaria abierta",
                    secondButtonText = if (!uiState.isOpen) "Caja cerrada" else "Cerrar caja",
                    firstButtonColor = if (uiState.isOpen) GreenText else GrayText,
                    secondButtonColor = if (uiState.isOpen) AccentColor else GrayText,
                    onFirstButtonClick = {
                        if (uiState.isOpen) return@GenericScreenTitleHeaderWithButtons
                        viewModel.cleanData()
                        showOpenFinanceDialog = true
                    },
                    onSecondButtonClick = {
                        if (!uiState.isOpen) return@GenericScreenTitleHeaderWithButtons
                        confirmMsg = "Seguro que deseas cerrar caja diaria?"
                        showConfirmDialog = true
                    }
                )
                if (uiState.isOpen) {
                    SummaryHeader(uiState.dailyData)
                    LastTransactionsCard(
                        modifier = Modifier.weight(
                            1f
                        )
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "La caja se encuentra cerrada, abrila desde el botón 'Abrir caja'",
                            style = MaterialTheme.typography.headlineSmall,
                            color = WhiteText
                        )
                    }
                }

            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(
                msg = confirmMsg,
                onAccept = {
                    showConfirmDialog = false
                    viewModel.closeFinance()
                },
                onDismiss = { showConfirmDialog = false }
            )
        }
        SimpleAdviceDialog(
            msg = adviceMsg,
            show = showAdviceDialog,
            onDismiss = { showAdviceDialog = false }
        )
        if (showOpenFinanceDialog) {
            OpenFinanceDialog(
                uiState.dailyData,
                onAccept = {
                    viewModel.openFinance { showOpenFinanceDialog = false }
                },
                onDismiss = {
                    showOpenFinanceDialog = false
                },
                onActionDone = { action, value ->
                    viewModel.updateDailyData(action, value)
                }
            )
        }
    }
}

@Composable
private fun OpenFinanceDialog(
    dailyData: DailyData,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    onActionDone: (DailyDataAction, String) -> Unit,

    ) {

    val initialCashAmount =
        if (dailyData.initialCashAmount == 0L) "" else dailyData.initialCashAmount.toString()
    val initialVirtualAccountAmount =
        if (dailyData.initialVirtualAccountAmount == 0L) "" else dailyData.initialVirtualAccountAmount.toString()

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
                        "Abrir caja diaria",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    GenericTextField(
                        initialCashAmount,
                        "Monto inicial en efectivo",
                        isPrice = true,
                        onlyNumbers = true
                    ) { onActionDone(UPDATE_CASH, it) }
                    GenericTextField(
                        initialVirtualAccountAmount,
                        "Monto inicial en cuenta virtual",
                        isPrice = true,
                        onlyNumbers = true
                    ) { onActionDone(UPDATE_VIRTUAL_ACCOUNT, it) }
                }
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
private fun LastTransactionsCard(modifier: Modifier) {

    var showSellDialog by rememberSaveable { mutableStateOf(false) }
    var showExpenseDialog by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryCardHeader(
                    icon = Icons.Outlined.CurrencyExchange,
                    title = "Transacciones",
                    description = "Transacciones entrantes y salientes del día"
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GenericButton(
                        text = "Nueva venta",
                        icon = Icons.Outlined.Add,
                        onClick = { showSellDialog = true }
                    )
                    GenericButton(
                        text = "Nuevo gasto",
                        icon = Icons.Outlined.Remove,
                        onClick = { }
                    )
                }
            }
        }
    }
}


@Composable
fun SummaryHeader(dailyData: DailyData) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MainCardSummary(
            Icons.Outlined.AccountBalance,
            totalAmount = 1500000,
            modifier = Modifier.weight(1f),
            title = "Balance",
            balanceText = "Ingresos - egresos del día:",
            description = "Balance del día",
            cashText = "Inicio de caja en efectivo:",
            cashAmount = dailyData.initialCashAmount,
            transferText = "Inicio de caja en cuenta virtual:",
            transferAmount = dailyData.initialVirtualAccountAmount,
            onClick = { },
        )
        MainCardSummary(
            icon = Icons.Outlined.Wallet,
            totalAmount = 250000,
            modifier = Modifier.weight(1f),
            title = "Ingresos",
            description = "Ingresos del día incluyendo efectivo y transferencias",
            cashText = "Ingresos en efectivo:",
            cashAmount = 15000,
            transferText = "Ingresos en cuenta virtual:",
            transferAmount = 156000,
            onClick = { },
        )
        MainCardSummary(
            icon = Icons.Outlined.Money,
            totalAmount = 250000,
            modifier = Modifier.weight(1f),
            title = "Egresos",
            description = "Egresos del día incluyendo efectivo y transferencias",
            cashText = "Egresos en efectivo:",
            cashAmount = 15000,
            transferText = "Egresos en cuenta virtual:",
            transferAmount = 156000,
            onClick = { },
        )
    }
}

@Composable
private fun MainCardSummary(
    icon: ImageVector,
    title: String,
    description: String,
    balanceText:String = "Total:",
    cashText: String,
    cashAmount: Long,
    transferText: String,
    transferAmount: Long,
    totalAmount: Long,
    modifier: Modifier,
    onClick: () -> Unit,
) {

    val amountColor = when {
        totalAmount == 0L -> WhiteText
        totalAmount > 0L -> GreenText
        else -> AccentColor
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(
                icon,
                title,
                description
            )
            Row {
                Text(
                    balanceText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    totalAmount.toPrice(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = amountColor
                )
            }
            Row {
                Text(
                    cashText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Spacer(Modifier.width(4.dp))

                Text(
                    cashAmount.toPrice(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = amountColor
                )
            }
            Row {
                Text(
                    transferText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    transferAmount.toPrice(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = amountColor
                )
            }
            Button(
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(horizontal = 0.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Ver detalles")
                    Icon(
                        Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}