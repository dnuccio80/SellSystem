package org.example.project.ui.screens.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import org.example.project.domain.models.Product
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.RowWithMidBodyAndDescription
import org.example.project.ui.RowWithSmallBodyAndDescription
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.addproducts.AddProductScreen
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sellsystem.shared.generated.resources.Res
import sellsystem.shared.generated.resources.woman_img
import java.io.File

class ProductsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val visualTypeList = listOf(
            "list",
            "gallery"
        )
        var visualTypeSelected by rememberSaveable { mutableStateOf(visualTypeList.last()) }

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
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        val firstSelected = visualTypeSelected == visualTypeList.first()

                        Text(
                            "Opciones de vista:",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = { visualTypeSelected = visualTypeList.first() },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = if (firstSelected) Color.White else GrayText,
                                containerColor = if (firstSelected) GreenText else Color.Transparent
                            ),
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ListAlt,
                                contentDescription = "ver por lista"
                            )
                        }
                        IconButton(
                            onClick = { visualTypeSelected = visualTypeList.last() },
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = if (!firstSelected) Color.White else GrayText,
                                containerColor = if (!firstSelected) GreenText else Color.Transparent
                            )
                        ) {
                            Icon(
                                Icons.Outlined.Image,
                                contentDescription = "ver por imagen"
                            )
                        }
                    }
                    AnimatedContent(visualTypeSelected) {
                        if (products.isNotEmpty()) {

                            if(visualTypeSelected == visualTypeList.first()) {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(products) {item ->
                                        ProductInListCardItem(item) { navigator?.push(AddProductScreen(item.id)) }
                                    }
                                }
                            }else {
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(250.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(products) { item ->
                                        ProductInLazyGridCardItem(item) {
                                            navigator?.push(
                                                AddProductScreen(
                                                    item.id
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Text(
                                "No hay productos disponibles",
                                style = MaterialTheme.typography.titleMedium,
                                color = WhiteText
                            )
                        }

                    }
                    if (products.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(250.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(products) { item ->
                                ProductInLazyGridCardItem(item) {
                                    navigator?.push(
                                        AddProductScreen(
                                            item.id
                                        )
                                    )
                                }
                            }
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
private fun ProductInLazyGridCardItem(product: Product, onClick: () -> Unit) {
    val stockManage = if (product.currentStock > 0 && product.adviceStock > 0) "Si" else "No"

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val cardColor = if (isHovered) SecondaryCardBackground else PrimaryCardBackground

    Card(
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ProductImage(
                product.imagePath,
                product.name,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    product.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
            RowWithSmallBodyAndDescription("Marca:", product.brand)
            RowWithSmallBodyAndDescription("Precio de lista:", product.listPrice.toPrice())
            RowWithSmallBodyAndDescription(
                "Efectivo/Transf:",
                product.cashPrice.toPrice()
            )
            if (product.manageStock) {
                RowWithSmallBodyAndDescription("En stock:", product.currentStock.toString())
            } else {
                RowWithSmallBodyAndDescription("Con control de stock:", stockManage)
            }
        }
    }
}

@Composable
private fun ProductInListCardItem(product: Product, onClick: () -> Unit) {

    val stockManage = if (product.currentStock > 0 && product.adviceStock > 0) "Si" else "No"

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val cardColor = if (isHovered) SecondaryCardBackground else PrimaryCardBackground

    Card(
        modifier = Modifier.fillMaxWidth().pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interactionSource).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RowWithMidBodyAndDescription("Producto:", product.name)
            RowWithMidBodyAndDescription("Categoría:", product.category)
            RowWithMidBodyAndDescription("Marca:", product.brand)
            RowWithMidBodyAndDescription("Precio de lista:", product.listPrice.toPrice())
            RowWithMidBodyAndDescription(
                "Precio en transferencia/efectivo:",
                product.cashPrice.toPrice()
            )
            if (product.manageStock) {
                RowWithMidBodyAndDescription("En stock:", product.currentStock.toString())
                RowWithMidBodyAndDescription(
                    "Advertencia de poco stock:",
                    product.adviceStock.toString()
                )
            } else {
                RowWithMidBodyAndDescription("Con control de stock:", stockManage)
            }
        }
    }
}

@Composable
fun ProductImage(
    imagePath: String?, productName: String, modifier: Modifier = Modifier,
) {
    if (imagePath != null) {
        AsyncImage(
            model = File(imagePath),
            contentDescription = productName,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(Res.drawable.woman_img),
            contentDescription = "Sin imagen",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}




