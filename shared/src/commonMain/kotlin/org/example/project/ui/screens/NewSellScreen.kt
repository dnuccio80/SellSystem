package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericScreenTitleHeaderWithButtons
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.toPrice
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText

@Composable
fun NewSellScreen() {
    ScreenContainer {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Header()
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                NewItemSell("Lima para uñas x1 unidad")
                NewItemSell("Quitaesmaltes x100ml Tortuguita")
                NewItemSell("Esmalte semipermanente x100ml marca Catunga")
            }
            Spacer(Modifier.weight(1f))
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Total:", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(150000L.toPrice(), style = MaterialTheme.typography.headlineSmall, color = GreenText, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GenericButton("Cancelar", color = GrayText) { }
                        GenericButton("Aceptar") { }
                    }
                }
            }
        }
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
                text = "Agregar item"
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
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(5f)) {
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
