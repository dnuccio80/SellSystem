package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.GenericButton
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText

@Composable
fun DashboardScreen() {
    ScreenContainer {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TitleHeader()
            HeaderCardSummary()
            MidCardSummary(Modifier.weight(1f))
            LowCardSummary(Modifier.weight(1f))
        }
    }
}

@Composable
private fun TitleHeader() {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                "Panel General",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                "Resumen de todos los datos",
                color = WhiteText,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenericButton("Este mes", Icons.Default.ArrowDropDown) { }
            GenericButton("Resetear datos") { }
        }
    }
}

@Composable
private fun HeaderCardSummary() {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SummaryCard(
            icon = Icons.Default.AccountBalance,
            title = "Balance",
            description = "Ingresos menos gastos",
            amount = 20000000,
            buttonText = "Ver detalles",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            icon = Icons.Default.Wallet,
            title = "Ingresos",
            description = "Ingresos incluyendo transferencias y efectivo",
            amount = 20000000,
            buttonText = "Ver detalles",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            icon = Icons.Filled.Money,
            title = "Gastos",
            description = "Egresos de dinero en efectivo y transferencias",
            amount = 0,
            buttonText = "Ver detalles",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MidCardSummary(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PendingOrdersSummaryCard(Modifier.weight(1f))
        LowStockCardSummary(Modifier.weight(1f))
    }
}

@Composable
private fun PendingOrdersSummaryCard(modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(
                Icons.Default.PendingActions,
                "Órdenes pendientes",
                "Ventas a despachar"
            )
        }
    }
}


@Composable
private fun SummaryCard(
    icon: ImageVector,
    title: String,
    description: String,
    amount: Long,
    buttonText: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {

    val amountColor = when {
        amount == 0L -> WhiteText
        amount > 0L -> GreenText
        else -> AccentColor
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(icon, title, description)
            Text(
                amount.toPrice(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = amountColor
            )
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
                    Text(buttonText)
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

@Composable
private fun SummaryCardHeader(icon: ImageVector, title: String, description: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.size(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = SecondaryCardBackground,
                contentColor = Color.White
            )
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(4.dp)
            )
        }
        Column {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(description, style = MaterialTheme.typography.labelLarge, color = WhiteText)
        }
    }
}

@Composable
private fun LowStockCardSummary(modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(
                Icons.Default.Inventory2,
                "Productos con bajo stock",
                "Productos sin stock o con poca cantidad en stock"
            )
        }
    }
}

@Composable
private fun CurrentAccountsCardSummary(modifier: Modifier){
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(
                Icons.Default.AccountBox,
                "Cuentas corrientes",
                "Saldos pendientes de clientes regulares"
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SecondaryCardBackground),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total cuentas corrientes",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            "2000",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = GreenText
                        )
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SecondaryCardBackground),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Monto total en cuentas corrientes",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            200000L.toPrice(),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = GreenText
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {  },
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
}

@Composable
private fun MostSellArticlesCardSummary(modifier: Modifier){
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            SummaryCardHeader(
                Icons.Default.Loyalty,
                "Articulos mas vendidos",
                "Top de productos mas vendidos historicamente"
            )
        }
    }
}
@Composable
fun LowCardSummary(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CurrentAccountsCardSummary(Modifier.weight(1f))
        MostSellArticlesCardSummary(Modifier.weight(1f))
    }
}
