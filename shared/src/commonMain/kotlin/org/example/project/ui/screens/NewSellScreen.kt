package org.example.project.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.project.ui.GenericButton
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText

@Composable
fun NewSellScreen() {
    ScreenContainer {

        var isUsualClient by rememberSaveable { mutableStateOf(false) }
        var showClientDialog by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Header()
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(2.dp, color = GrayText),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    NewItemSell("Lima para uñas x1 unidad")
                    NewItemSell("Quitaesmaltes x100ml Tortuguita")
                    NewItemSell("Esmalte semipermanente x100ml marca Catunga")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier.clickable { isUsualClient = !isUsualClient },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isUsualClient,
                        onCheckedChange = {
                            isUsualClient = !isUsualClient
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = GreenText,
                            uncheckedColor = GrayText
                        )
                    )
                    Text(
                        "Es cliente usual",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                AnimatedContent(isUsualClient) {
                    if (isUsualClient) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            GenericButton("Seleccionar cliente") { showClientDialog = true }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("Cliente seleccionado:", fontWeight = FontWeight.SemiBold, color = Color.White)
                                Text("Laura Cana", fontWeight = FontWeight.SemiBold, color = GreenText)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Total:",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        150000L.toPrice(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = GreenText,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GenericButton("Cancelar", color = GrayText) { }
                        GenericButton("Aceptar") { }
                    }
                }
            }
        }

        if (showClientDialog) {
            Dialog(
                onDismissRequest = { showClientDialog = false }

            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().height(500.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(onClick = { showClientDialog = false }) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "close dialog",
                                tint = Color.White
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        TextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Buscar cliente...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = SecondaryCardBackground,
                                unfocusedContainerColor = SecondaryCardBackground,
                                focusedIndicatorColor = GreenText,
                                unfocusedIndicatorColor = GrayText,
                                cursorColor = GreenText,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                unfocusedPlaceholderColor = WhiteText,
                                focusedPlaceholderColor = Color.White
                            )
                        )
                        Spacer(Modifier.size(12.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            ClientItem("Leysa Asnal")
                            ClientItem("Laura Cana")
                            ClientItem("Florencia Medina")
                        }
                    }
                }
            }
        }
    }


//    Dialog(
//        onDismissRequest = { },
//    ) {
//        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground)) {
//            Column(Modifier.fillMaxWidth().padding(16.dp)) {
//                Text("Agregar Items")
//            }
//        }
//    }
}

@Composable
private fun ClientItem(name: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = SecondaryCardBackground,
            contentColor = Color.White
        )
    ) {
        Text(
            name,
            color = WhiteText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun Header() {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                "Nueva venta",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                "Detalles de nueva venta",
                color = WhiteText,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GenericButton(
                text = "Agregar items"
            ) {

            }
        }
    }

}

@Composable
private fun NewItemSell(product: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(5f)
        ) {
            Text(
                product,
                color = WhiteText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.weight(5f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cant.", color = WhiteText, maxLines = 1)
                IconButton(
                    onClick = { },
                ) {
                    Icon(
                        Icons.Outlined.Remove,
                        contentDescription = "reduce by 1",
                        tint = Color.White
                    )
                }
                TextField(
                    value = "1", onValueChange = { },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SecondaryCardBackground,
                        unfocusedContainerColor = SecondaryCardBackground,
                        focusedIndicatorColor = GreenText,
                        unfocusedIndicatorColor = GrayText,
                        unfocusedTextColor = WhiteText,
                        focusedTextColor = WhiteText,
                        cursorColor = GreenText
                    ),
                    modifier = Modifier.width(80.dp).height(55.dp),
                    singleLine = true,
                    maxLines = 1,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                IconButton(
                    onClick = { },
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "plus by 1", tint = Color.White)
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("Subtotal:", color = WhiteText, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(12500L.toPrice(), color = GreenText, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}
