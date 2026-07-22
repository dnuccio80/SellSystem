package org.example.project.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericTextField
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SearchTextField
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.WhiteText

class ProductsScreen : Screen {
    @Composable
    override fun Content() {

        var showAddProductDialog by rememberSaveable { mutableStateOf(false) }
        var isVariableProduct by rememberSaveable { mutableStateOf(false) }
        var manageStock by rememberSaveable { mutableStateOf(false) }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Header { showAddProductDialog = true }
            }

            if(showAddProductDialog) {
                AddProductDialog(
                    manageStock = manageStock,
                    isVariableProduct = isVariableProduct,
                    onToggleManageStock = { manageStock = !manageStock },
                    onToggleVariableProduct = { isVariableProduct = !isVariableProduct },
                    onDismiss = { showAddProductDialog = false }
                )
            }
        }
    }
}

@Composable
private fun AddProductDialog(manageStock: Boolean, isVariableProduct: Boolean, onToggleManageStock:() -> Unit, onToggleVariableProduct:() -> Unit, onDismiss:() -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth().height(550.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Agregar nuevo producto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier.verticalScroll(
                        rememberScrollState()
                    )
                ) {
                    GenericTextField("", "Nombre") { }
                    GenericTextField("", "Descripción") { }
                    GenericTextField("", "Marca") { }
                    GenericTextField("", "Precio de compra") { }
                    GenericTextField("", "Precio de venta") { }
                    GenericTextField("", "Categoría") { }
                    GenericTextField("", "Proveedor") { }
                    CheckBoxItem("Gestionar stock", manageStock) {
                        onToggleManageStock()
                    }
                    AnimatedContent(manageStock) {
                        if (manageStock) {
                            Column {
                                GenericTextField("", "Stock") { }
                                GenericTextField(
                                    "",
                                    "Cantidad para notificar poco stock"
                                ) { }
                            }
                        }
                    }
                    CheckBoxItem(
                        "Producto con variantes",
                        isVariableProduct
                    ) { onToggleVariableProduct }
                    AnimatedContent(isVariableProduct) {
                        if (isVariableProduct) {
                            Column(Modifier.padding(horizontal = 16.dp)) {
                                CheckBoxItem(
                                    "Gestionar color",
                                    checked = false,
                                    onClick = { },
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Margen de ganancia:",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "54%",
                                style = MaterialTheme.typography.titleMedium,
                                color = GreenText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    AcceptDeclineButtons(onAccept = { }, onDismiss = { onDismiss() })
                }

            }
        }

    }
}

@Composable
private fun CheckBoxItem(name: String, checked: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClick()
        }) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = GreenText,
                uncheckedColor = GrayText
            )
        )
        Text(name, color = Color.White)
    }
}



@Composable
private fun Header(onButtonClick: () -> Unit) {
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
                    "Productos",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    "Listado de todos los productos con y sin stock",
                    color = WhiteText,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            SearchTextField("", onValueChange = { })
        }
        GenericButton(
            text = "Agregar producto"
        ) {
            onButtonClick()
        }
    }

}