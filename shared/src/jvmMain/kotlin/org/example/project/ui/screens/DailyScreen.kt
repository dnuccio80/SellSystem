package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericScreenTitleHeaderWithButtons
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SummaryCardHeader
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText

class DailyScreen : Screen {
    @Composable
    override fun Content() {
        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericScreenTitleHeaderWithButtons(
                    mainTitle = "Caja diaria",
                    description = "Movimientos de dinero del día",
                    firstButtonText = "Abrir caja",
                    secondButtonText = "Cerrar caja",
                    onFirstButtonClick = { },
                    onSecondButtonClick = { }
                )
                SummaryHeader()
                LastTransactionsCard(
                    modifier = Modifier.weight(
                        1f
                    )
                )
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
fun SummaryHeader() {
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
            description = "Balance del día incluyendo inicio de caja",
            cashText = "Inicio de caja en efectivo: ",
            cashAmount = 250000,
            transferText = "Inicio de caja en cuenta virtual: ",
            transferAmount = 156000,
            onClick = { },
        )
        MainCardSummary(
            icon = Icons.Outlined.Wallet,
            totalAmount = 250000,
            modifier = Modifier.weight(1f),
            title = "Ingresos",
            description = "Ingresos del día incluyendo efectivo y transferencias",
            cashText = "Ingresos en efectivo: ",
            cashAmount = 15000,
            transferText = "Ingresos en cuenta virtual: ",
            transferAmount = 156000,
            onClick = { },
        )
        MainCardSummary(
            icon = Icons.Outlined.Money,
            totalAmount = 250000,
            modifier = Modifier.weight(1f),
            title = "Egresos",
            description = "Egresos del día incluyendo efectivo y transferencias",
            cashText = "Egresos en efectivo: ",
            cashAmount = 15000,
            transferText = "Egresos en cuenta virtual: ",
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
                    "Total: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
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