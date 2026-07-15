package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Filter1
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import sellsystem.shared.generated.resources.Res
import sellsystem.shared.generated.resources.compose_multiplatform
import sellsystem.shared.generated.resources.woman_img

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
                                ContentContainer()
                            }
                        }
                    }
                }
            }
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
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = PrimaryBackground)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 32.dp)) {
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
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), colors = CardDefaults.cardColors(containerColor = CardTitleBackground)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Checkbox(
                                checked = false,
                                onCheckedChange = { },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = GrayText,
                                    checkedColor = GreenText
                                )
                            )
                            Text("SKU", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("Imagen", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("Titulo", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("Categoria", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("Cantidad", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("Precio", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("Ult. modif.", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("Estado", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            IconButton(onClick = { }) {
                                Icon(Icons.Outlined.FilterAlt, contentDescription = "filter button", tint = Color.White)
                            }
                        }
                    }

                }
            }
        }
    }
}
