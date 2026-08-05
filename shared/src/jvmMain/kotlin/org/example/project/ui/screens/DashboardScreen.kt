package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Loyalty
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.PendingActions
import androidx.compose.material.icons.outlined.Wallet
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.ui.GenericScreenTitleHeaderWithButtons
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SummaryCard
import org.example.project.ui.SummaryCardHeader
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground


class DashboardScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericScreenTitleHeaderWithButtons(
                    mainTitle = "Panel General",
                    description = "Resumen de todos los datos",
                    firstButtonText = "Este mes",
                    secondButtonText = "Resetear datos",
                    buttonIcon = Icons.Outlined.KeyboardArrowDown,
                    onFirstButtonClick = {},
                    onSecondButtonClick = { }
                )
             HeaderCardSummary()
             MidCardSummary(Modifier.weight(1f))
             LowCardSummary(Modifier.weight(1f))
            }
        }
    }

}

@Composable
fun HeaderCardSummary() {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SummaryCard(
            icon = Icons.Outlined.AccountBalance,
            title = "Balance",
            description = "Ingresos menos gastos",
            amount = 20000000,
            buttonText = "Ver detalles",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            icon = Icons.Outlined.Wallet,
            title = "Ingresos",
            description = "Ingresos incluyendo transferencias y efectivo",
            amount = 20000000,
            buttonText = "Ver detalles",
            onClick = { },
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            icon = Icons.Outlined.Money,
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
       PendingOrdersSummaryCard(
            Modifier.weight(
                1f
            )
        )
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
                Icons.Outlined.PendingActions,
                "Órdenes pendientes",
                "Ventas a despachar"
            )
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
                Icons.AutoMirrored.Outlined.List,
                "Lista de tareas",
                "Tareas para realizar"
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
                Icons.Outlined.AccountBox,
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
                            Icons.AutoMirrored.Outlined.ArrowForwardIos,
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
                Icons.Outlined.Loyalty,
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
        CurrentAccountsCardSummary(
            Modifier.weight(
                1f
            )
        )
       MostSellArticlesCardSummary(
            Modifier.weight(
                1f
            )
        )
    }
}
