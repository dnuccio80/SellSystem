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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import org.example.project.domain.models.product.Product
import org.example.project.domain.models.product.ProductCategory
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization.SENTENCES
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericTextField
import org.example.project.ui.RowWithMidBodyAndDescription
import org.example.project.ui.RowWithSmallBodyAndDescription
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SearchTextField
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.addproducts.AddProductScreen
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sellsystem.shared.generated.resources.Res
import sellsystem.shared.generated.resources.generic_image
import java.io.File


enum class ProductLabel(val etiquette: String) {
    PRODUCT("Productos"), CATEGORIES("Categorías"), VARIANTS("Variantes")
}

class ProductsScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewmodel = koinViewModel<ProductsViewModel>()
        val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
        val query by viewmodel.query.collectAsStateWithLifecycle()
        var adviceMsg by rememberSaveable { mutableStateOf("") }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var showAddCategoryDialog by rememberSaveable { mutableStateOf(false) }
        var isEditCategory by rememberSaveable { mutableStateOf(false) }

        val labelList = listOf(
            ProductLabel.PRODUCT.etiquette,
            ProductLabel.CATEGORIES.etiquette,
            ProductLabel.VARIANTS.etiquette,
        )

        var labelSelected by rememberSaveable { mutableStateOf(labelList.first()) }

        LaunchedEffect(viewmodel.events) {
            viewmodel.events.collect { msg ->
                adviceMsg = msg
                showAdviceDialog = true
            }
        }

        when (uiState) {
            is ProductUiState.Error -> {}
            ProductUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is ProductUiState.Success -> {
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
                            onDeleteQuerySearch = { viewmodel.updateQuery("") },
                            onSearchValueChange = { viewmodel.updateQuery(it) },
                            onButtonClick = {
                                when (labelSelected) {
                                    ProductLabel.PRODUCT.etiquette -> navigator?.push(
                                        AddProductScreen()
                                    )

                                    ProductLabel.CATEGORIES.etiquette -> {
                                        isEditCategory = false
                                        showAddCategoryDialog = true
                                    }
                                }
                            },
                            onLabelChange = {
                                labelSelected = it
                                viewmodel.updateQuery("")
                            }
                        )
                        when (labelSelected) {
                            ProductLabel.PRODUCT.etiquette -> ProductContent((uiState as ProductUiState.Success).products) {
                                navigator?.push(
                                    AddProductScreen(it)
                                )
                            }

                            ProductLabel.CATEGORIES.etiquette -> CategoriesContent((uiState as ProductUiState.Success).categories) { id ->
                                viewmodel.getCategory(id) {
                                    isEditCategory = true
                                    showAddCategoryDialog = true
                                }
                            }
                        }

                    }
                    if (showAdviceDialog) {
                        SimpleAdviceDialog(adviceMsg, showAdviceDialog) { showAdviceDialog = false }
                    }
                    if (showAddCategoryDialog) {
                        AddCategoryDialog(
                            value = (uiState as ProductUiState.Success).newCategory.name,
                            isEditCategory,
                            onDismiss = {
                                showAddCategoryDialog = false
                                viewmodel.cleanCategory()
                            },
                            onAccept = {
                                viewmodel.tryAddCategory {
                                    showAddCategoryDialog = false
                                }
                            },
                            onValueChange = { viewmodel.updateCategoryName(it) },
                            onDelete = {
                                viewmodel.deleteCategory()
                                showAddCategoryDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddCategoryDialog(
    value: String,
    isEdit: Boolean,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onDelete: () -> Unit,
) {

    val text = if (isEdit) "Editar categoría" else "Agregar nueva categoría"
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isEdit) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                text,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        GenericButton("Eliminar", color = AccentColor) { showConfirmDialog = true }
                    }
                } else {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                GenericTextField(
                    value,
                    "Nombre de la categoría",
                    capitalizationMethod = SENTENCES
                ) { onValueChange(it) }
                Spacer(Modifier.size(0.dp))
                AcceptDeclineButtons(
                    acceptColor = GreenText,
                    onAccept = { onAccept() },
                    onDismiss = { onDismiss() })
            }
        }
        if (showConfirmDialog) {
            ConfirmDialog(
                "Seguro que deseas eliminar la categoría?",
                onAccept = {
                    onDelete()
                    showConfirmDialog = false
                },
                onDismiss = { showConfirmDialog = false }
            )
        }
    }
}

@Composable
private fun CategoriesContent(categories: List<ProductCategory>, onClick: (Int) -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val cardColor = if (isHovered) SecondaryCardBackground else PrimaryCardBackground

    Text(
        "Listado de categorías",
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (categories.isNotEmpty()) {
            items(categories) { category ->
                Card(
                    modifier = Modifier.fillMaxWidth().pointerHoverIcon(PointerIcon.Hand)
                        .hoverable(interactionSource).clickable { onClick(category.id) },
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.elevatedCardElevation(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            category.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            item {
                Text(
                    "No hay categorías disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    color = WhiteText
                )
            }
        }

    }
}


@Composable
private fun ProductContent(products: List<Product>, onNavigateToItem: (Int) -> Unit) {

    val visualTypeList = listOf(
        "list",
        "gallery"
    )
    var visualTypeSelected by rememberSaveable { mutableStateOf(visualTypeList.last()) }

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

                if (visualTypeSelected == visualTypeList.first()) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(products) { item ->
                            ProductInListCardItem(item) {
                                onNavigateToItem(item.id)
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(250.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(products) { item ->
                            ProductInLazyGridCardItem(item) {
                                onNavigateToItem(item.id)

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
    }
}

@Composable
private fun Header(
    searchValue: String,
    labelList: List<String>,
    labelSelected: String,
    onDeleteQuerySearch: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    onLabelChange: (String) -> Unit,
) {

    val buttonText = when (labelSelected) {
        ProductLabel.PRODUCT.etiquette -> "Agregar producto"
        ProductLabel.CATEGORIES.etiquette -> "Agregar categoría"
        else -> "Agregar variante"
    }

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
                    "Listado de todos los productos, con y sin stock",
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
                    label,
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
            text = buttonText,
        ) {
            onButtonClick()
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
            painter = painterResource(Res.drawable.generic_image),
            contentDescription = "Sin imagen",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}




