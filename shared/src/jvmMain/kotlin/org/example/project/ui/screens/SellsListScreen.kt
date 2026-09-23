package org.example.project.ui.screens

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Loyalty
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.PersonPinCircle
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.domain.models.sell.Sell
import org.example.project.domain.usecases.sells.SellFilterLabel
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization.SENTENCES
import org.example.project.ui.GenericButton
import org.example.project.ui.RowWithMidBodyAndDescription
import org.example.project.ui.RowWithMidTitleAndDescription
import org.example.project.ui.RowWithSmallBodyAndDescription
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SearchTextField
import org.example.project.ui.ext.formatToDisplay
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.sells.SellListViewModel
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.LightBlue
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel

class SellsListScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<SellListViewModel>()

        val query by viewModel.query.collectAsStateWithLifecycle()
        val sellsList by viewModel.sellsList.collectAsStateWithLifecycle()
        val labelSelected by viewModel.labelSelected.collectAsStateWithLifecycle()
        val sellData by viewModel.sellData.collectAsStateWithLifecycle()
        var showDataSell by rememberSaveable { mutableStateOf(false) }

        val labelList = listOf(
            SellFilterLabel.ALL,
            SellFilterLabel.TODAY,
            SellFilterLabel.WEEK,
            SellFilterLabel.MONTH,
        )

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Header(
                    searchValue = query,
                    labelList,
                    labelSelected,
                    onDeleteQuerySearch = { viewModel.updateQuery("") },
                    onSearchValueChange = { viewModel.updateQuery(it) },
                    onButtonClick = { navigator?.push(NewSellScreen()) },
                    onLabelChange = {
                        viewModel.updateLabelSelected(it)
                        viewModel.updateQuery("")
                    }
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (sellsList.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(5f)
                        ) {
                            items(sellsList) { sell ->
                                SellCardItem(sell) {
                                    viewModel.getSell(sell.id) {
                                        showDataSell = true
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            "No hay ventas en la búsqueda",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(5f)
                        )
                    }
                    SummaryCardColumn(Modifier.weight(1f))
                    if (showDataSell) {
                        SellDataDialog(
                            sellData!!,
                            onDismiss = {
                                showDataSell = false
                                viewModel.clearSellData()
                            },
                            onDeleteSell = {
                                showDataSell = false
                                viewModel.deleteSell()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    searchValue: String,
    labelList: List<SellFilterLabel>,
    labelSelected: SellFilterLabel,
    onDeleteQuerySearch: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onLabelChange: (SellFilterLabel) -> Unit,
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
                    "Ventas",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    "Listado de ventas históricas",
                    color = WhiteText,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            SearchTextField(
                searchValue,
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
            labelList.forEach { label ->
                Text(
                    label.etiquette,
                    modifier = Modifier.clickable { onLabelChange(label) }
                        .pointerHoverIcon(PointerIcon.Hand),
                    color = if (label == labelSelected) GreenText else Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
                if (label != labelList.last()) {
                    VerticalDivider(thickness = 2.dp, color = GrayText)
                }
            }
        }
        GenericButton(
            text = "Nueva venta",
        ) {
            onButtonClick()
        }
    }
}


@Composable
private fun SummaryCardColumn(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SummaryCardRowItem(
            modifier = Modifier.weight(1f),
            label = "Total ingresos",
            description = 250000L.toPrice(),
            icon = Icons.Outlined.Paid,
        )
        SummaryCardRowItem(
            modifier = Modifier.weight(1f),
            label = "Ventas totales",
            description = "250",
            icon = Icons.Outlined.Storefront,
        )
        SummaryCardRowItem(
            modifier = Modifier.weight(1f),
            label = "Ventas a clientes recurrentes",
            description = "50",
            icon = Icons.Outlined.Loyalty,
        )
        SummaryCardRowItem(
            modifier = Modifier.weight(1f),
            label = "Ventas a clientes genéricos",
            description = "200",
            icon = Icons.Outlined.PersonPinCircle,
        )
    }
}

@Composable
private fun SellDataDialog(sell: Sell, onDismiss: () -> Unit, onDeleteSell: () -> Unit) {

    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Venta Realizada",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        IconButton(modifier = Modifier.size(24.dp), onClick = { onDismiss() }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close dialog",
                                tint = Color.White
                            )
                        }
                    }
                }
                RowWithSmallBodyAndDescription(
                    "Fecha:",
                    description = sell.date!!.formatToDisplay()
                )
                RowWithSmallBodyAndDescription(
                    "Cliente:",
                    description = sell.clientName.ifBlank { "Genérico" })
                RowWithSmallBodyAndDescription("Método de pago:", description = sell.paymentMethod)
                Column(
                ) {
                    Text("Listado de productos", color = Color.White, maxLines = 1, overflow = TextOverflow.Visible)
                    HorizontalDivider(thickness = 2.dp, color = GreenText)
                }
                Column(
                    modifier = Modifier.heightIn(max = 250.dp).verticalScroll(
                        rememberScrollState()
                    )
                ) {
                    Text(
                        sell.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    RowWithMidTitleAndDescription("Total:", sell.total.toPrice())
                }
                AcceptDeclineButtons(
                    "Aceptar",
                    declineText = "Eliminar",
                    acceptColor = GreenText,
                    declineColor = AccentColor,
                    onDismiss = { showConfirmDialog = true },
                    onAccept = { onDismiss() },
                )
                if (showConfirmDialog) {
                    ConfirmDialog(
                        "Seguro que deseas eliminar la venta?",
                        onAccept = {
                            showConfirmDialog = false
                            onDeleteSell()
                        },
                        onDismiss = {
                            showConfirmDialog = false
                        })
                }
            }
        }
    }
}


@Composable
private fun SummaryCardRowItem(
    modifier: Modifier,
    label: String,
    description: String,
    icon: ImageVector,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryCardBackground),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = "", tint = Color.White, modifier = Modifier.size(50.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(label, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                Text(
                    description,
                    color = LightBlue,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Composable
private fun SellCardItem(sell: Sell, onClick: () -> Unit) {

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
                "Cliente: ${sell.clientName.ifBlank { "Genérico" }}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Fecha: ${sell.date!!.formatToDisplay()}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Total:",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        sell.total.toPrice(),
                        fontWeight = FontWeight.Bold,
                        color = GreenText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
