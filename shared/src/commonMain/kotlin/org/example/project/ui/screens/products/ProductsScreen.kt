package org.example.project.ui.screens.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.domain.models.Product
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.RowWithMidBodyAndDescription
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.addproducts.AddProductScreen
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel

class ProductsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewmodel = koinViewModel<ProductsViewModel>()
        val products by viewmodel.products.collectAsStateWithLifecycle()
        val query by viewmodel.query.collectAsStateWithLifecycle()
        var adviceMsg by rememberSaveable { mutableStateOf("") }

        var showAddProductDialog by rememberSaveable { mutableStateOf(false) }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Productos",
                    description = "Listado de todos los productos con y sin stock",
                    searchValue = query,
                    onSearchValueChange = { viewmodel.updateQuery(it) },
                    onDeleteQuerySearch = { viewmodel.updateQuery("") },
                    buttonText = "Agregar producto"
                ) { navigator?.push(AddProductScreen()) }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.verticalScroll(rememberScrollState())) {
                    if (products.isNotEmpty()) {
                        products.forEach { item ->
                            ProductCardItem(item) { navigator?.push(AddProductScreen(item.id)) }
                        }
                    } else {
                        Text(
                            "No hay productos disponibles",
                            style = MaterialTheme.typography.titleMedium,
                            color = WhiteText
                        )
                    }
                }
            }
            if (showAddProductDialog) {
                SimpleAdviceDialog(adviceMsg, showAdviceDialog) { showAdviceDialog = false }
            }
        }
    }
}

@Composable
private fun ProductCardItem(product: Product, onClick:() -> Unit) {

    val stockManage = if(product.currentStock > 0 && product.adviceStock > 0) "Si" else "No"

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val cardColor = if (isHovered) SecondaryCardBackground else PrimaryCardBackground

    Card(
        modifier = Modifier.fillMaxWidth().pointerHoverIcon(PointerIcon.Hand).hoverable(interactionSource).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            RowWithMidBodyAndDescription("Producto:", product.name)
            RowWithMidBodyAndDescription("Descripción:", product.description)
            RowWithMidBodyAndDescription("Categoría:", product.category)
            RowWithMidBodyAndDescription("Marca:", product.brand)
            RowWithMidBodyAndDescription("Precio de lista:", product.listPrice.toPrice())
            RowWithMidBodyAndDescription("Precio en transferencia/efectivo:", product.cashPrice.toPrice())
            if(product.manageStock) {
                RowWithMidBodyAndDescription("En stock:", product.currentStock.toString())
                RowWithMidBodyAndDescription("Advertencia de poco stock:", product.adviceStock.toString())
            } else {
                RowWithMidBodyAndDescription("Con control de stock:", stockManage)
            }
        }
    }
}




