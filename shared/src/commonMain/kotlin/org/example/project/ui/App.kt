package org.example.project.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.CardTitleBackground
import org.example.project.ui.utils.FullCard
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize().background(FullCard)) {
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
            ) {
                Scaffold(containerColor = FullCard) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column {
                            MainHeader()
                            Row {
                                SideBar()
                                DashboardScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen() {
    ScreenContainer {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    amount = -20000000,
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
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
                    modifier = Modifier.weight(1f).fillMaxHeight()
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
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
                    modifier = Modifier.weight(1f).fillMaxHeight()
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
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
                    modifier = Modifier.weight(1f).fillMaxHeight()
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
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
                    modifier = Modifier.weight(1f).fillMaxHeight()
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
        }
    }
}

@Composable
fun SummaryCard(
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
fun SideBar() {
    Box(modifier = Modifier.background(FullCard)) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = WhiteText
            )
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = WhiteText
            )
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = WhiteText
            )
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = WhiteText
            )
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = WhiteText
            )
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = WhiteText
            )
        }
    }
}

@Composable
fun ContentContainer() {
    ScreenContainer {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Panel principal",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, GrayText),
                ) {
                    Text("Boton prueba", style = MaterialTheme.typography.bodyLarge)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, GrayText),
                ) {
                    Text("Boton prueba", style = MaterialTheme.typography.bodyLarge)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, GrayText),
                ) {
                    Text("Boton prueba", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        Card(shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth()) {
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = CardTitleBackground)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = { },
                            colors = CheckboxDefaults.colors(
                                uncheckedColor = GrayText,
                                checkedColor = GreenText
                            )
                        )
                        Text(
                            "SKU",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Imagen",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Titulo",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Categoria",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Cantidad",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Precio",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Ult. modif.",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        Text(
                            "Estado",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Outlined.FilterAlt,
                                contentDescription = "filter button",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
