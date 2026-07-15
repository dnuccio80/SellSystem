package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sellsystem.shared.generated.resources.Res
import sellsystem.shared.generated.resources.woman_img

@Composable
fun MainHeader() {
    Column(modifier = Modifier.safeContentPadding().fillMaxWidth()) {
        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = WhiteText)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "INVENTORY",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                TextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Buscar") },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.Search, contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedPlaceholderColor = WhiteText,
                        unfocusedPlaceholderColor = WhiteText,
                        focusedTrailingIconColor = WhiteText,
                        unfocusedTrailingIconColor = WhiteText,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = GreenText,
                        unfocusedIndicatorColor = GrayText,
                        cursorColor = GreenText
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "notificaciones", tint = Color.White)
                }
                Card(shape = CircleShape, elevation = CardDefaults.cardElevation(4.dp)) {
                    Image(painterResource(Res.drawable.woman_img), contentDescription = null,  modifier = Modifier.size(40.dp))
                }
            }
        }
        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = WhiteText)
    }

}