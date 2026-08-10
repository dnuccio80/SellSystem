package org.example.project.ui.screens.expenses

import androidx.collection.CircularArray
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.domain.models.expense.ComposedExpenseFinance
import org.example.project.domain.models.expense.Expense
import org.example.project.domain.models.expense.ExpenseFinanceData
import org.example.project.ui.AcceptDeclineButtons
import org.example.project.ui.Capitalization
import org.example.project.ui.GenericButton
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.GenericTextField
import org.example.project.ui.RowWithMidTitleAndDescription
import org.example.project.ui.ScreenContainer
import org.example.project.ui.ext.formatToDisplay
import org.example.project.ui.ext.toPercentAdd
import org.example.project.ui.ext.toPrice
import org.example.project.ui.screens.clients.ConfirmDialog
import org.example.project.ui.screens.clients.SimpleAdviceDialog
import org.example.project.ui.screens.expenses.ExpensesUiState.Success
import org.example.project.ui.utils.AccentColor
import org.example.project.ui.utils.GrayText
import org.example.project.ui.utils.GreenText
import org.example.project.ui.utils.PrimaryCardBackground
import org.example.project.ui.utils.SecondaryCardBackground
import org.example.project.ui.utils.WhiteText
import org.koin.compose.viewmodel.koinViewModel


class ExpensesScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ExpensesViewModel>()
        var showNewExpenseDialog by rememberSaveable { mutableStateOf(false) }

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var isEditDialog by rememberSaveable { mutableStateOf(false) }

//        val expenseData by viewModel.expenseData.collectAsStateWithLifecycle()
//        val expensesList by viewModel.expenses.collectAsStateWithLifecycle()
//        val query by viewModel.query.collectAsStateWithLifecycle()
//        val financeData by viewModel.composedFinance.collectAsStateWithLifecycle()


        var adviceMsg by rememberSaveable { mutableStateOf("") }
        var showAdviceDialog by rememberSaveable { mutableStateOf(false) }
        var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(viewModel.events) {
            viewModel.events.collect { msg ->
                adviceMsg = msg
                showAdviceDialog = true
            }
        }

        when (uiState) {
            is ExpensesUiState.Error -> {}
            ExpensesUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is Success -> {
                ScreenContainer {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        GenericHeaderWithButtonAndSearch(
                            title = "Gastos",
                            description = "Listado de todos los gastos históricos",
                            buttonText = "Agregar gasto",
                            searchValue = (uiState as Success).query,
                            onSearchValueChange = { viewModel.updateQuery(it) },
                            onDeleteQuerySearch = { viewModel.updateQuery("") }
                        ) {
                            isEditDialog = false
                            showNewExpenseDialog = true
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            ExpensesListCardItem(
                                modifier = Modifier.weight(1f),
                                expensesList = (uiState as Success).expenses,
                                onExpenseClick = { id ->
                                    viewModel.getExpenseDataFromId(id) {
                                        isEditDialog = true
                                        showNewExpenseDialog = true
                                    }
                                },
                                onLabelChange = { viewModel.updateLabel(it) },
                                labelFilterList = viewModel.labelFilterList,
                                labelFilterSelected =  (uiState as Success).labelSelected
                            )
                            ExpenseFinance((uiState as Success).composedFinance, modifier = Modifier.weight(.5f))
                        }
                    }

                    if (showNewExpenseDialog) {
                        AddExpenseDialog(
                            expenseData = (uiState as Success).expenseData,
                            isEdit = isEditDialog,
                            onDismiss = {
                                showNewExpenseDialog = false
                                viewModel.cleanExpenseData()
                            },
                            onDescriptionChange = { viewModel.updateDescription(it) },
                            onAmountChange = { viewModel.updateAmount(it) },
                            onDeleteExpense = { showConfirmDialog = true },
                            onAccept = { viewModel.addExpense { showNewExpenseDialog = false } }
                        )
                    }
                    SimpleAdviceDialog(
                        msg = adviceMsg,
                        show = showAdviceDialog,
                        onDismiss = { showAdviceDialog = false }
                    )
                    if (showConfirmDialog) {
                        ConfirmDialog(
                            msg = "Seguro que deseas eliminar el gasto?",
                            onAccept = {
                                showConfirmDialog = false
                                showNewExpenseDialog = false
                                viewModel.deleteExpense()
                            },
                            onDismiss = {
                                showConfirmDialog = false
                            }
                        )
                    }
                }
            }
        }
    }


    @Composable
    private fun AddExpenseDialog(
        expenseData: Expense,
        isEdit: Boolean,
        onDescriptionChange: (String) -> Unit,
        onAmountChange: (String) -> Unit,
        onDeleteExpense: () -> Unit,
        onDismiss: () -> Unit,
        onAccept: () -> Unit,
    ) {

        val amount = if (expenseData.amount == 0L) "" else expenseData.amount.toString()

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
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        if (isEdit) GenericButton(
                            "Eliminar",
                            color = AccentColor
                        ) { onDeleteExpense() }
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                if (isEdit) "Modificar gasto" else "Agregar nuevo gasto",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Column {
                        GenericTextField(
                            expenseData.description,
                            "Descripción",
                            capitalizationMethod = Capitalization.SENTENCES
                        ) { onDescriptionChange(it) }
                        GenericTextField(
                            value = amount,
                            labelText = "Monto",
                            isPrice = true,
                            onlyNumbers = true
                        ) { onAmountChange(it) }
                    }
                    if (isEdit) {
                        Text(
                            "Última modif: ${expenseData.date?.formatToDisplay()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText
                        )
                    }
                    AcceptDeclineButtons(onAccept = { onAccept() }, onDismiss = { onDismiss() })
                }
            }

        }
    }
}

@Composable
private fun ExpenseFinance(financeData: ComposedExpenseFinance, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        ExpensesFinanceResume(
            Modifier.fillMaxWidth().weight(1f),
            title = "Resumen del mes",
            financeData.monthExpenseData
        )
        ExpensesFinanceResume(
            Modifier.fillMaxWidth().weight(1f),
            title = "Mes anterior",
            financeData.previousMonthExpenseData
        )
        ExpensesFinanceResume(
            Modifier.fillMaxWidth().weight(1f),
            title = "Resumen del trimestre",
            financeData.threeMonthExpenseData
        )
    }
}

@Composable
fun ExpensesListCardItem(
    modifier: Modifier,
    expensesList: List<Expense>,
    labelFilterList: List<String>,
    labelFilterSelected: String,
    onExpenseClick: (Int) -> Unit,
    onLabelChange: (String) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            if (expensesList.isNotEmpty()) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Listado de gastos",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
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
                            labelFilterList.forEach { label ->
                                Text(
                                    label,
                                    modifier = Modifier.clickable { onLabelChange(label) }
                                        .pointerHoverIcon(PointerIcon.Hand),
                                    color = if (label == labelFilterSelected) GreenText else Color.White,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                if (label != labelFilterList.last()) {
                                    VerticalDivider(thickness = 2.dp, color = GrayText)
                                }
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.5.dp, color = WhiteText)
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(expensesList) { expense ->
                        ExpenseCardItem(expense) {
                            onExpenseClick(expense.id)
                        }
                    }
                }
            } else {
                Text(
                    "No hay gastos de momento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun ExpenseCardItem(expense: Expense, onClick: () -> Unit) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val cardColor = if (isHovered) GreenText.copy(alpha = .2f) else SecondaryCardBackground

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
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    expense.description,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    expense.date!!.formatToDisplay(),
                    style = MaterialTheme.typography.labelSmall,
                    color = GrayText
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                expense.amount.toPrice(),
                fontWeight = FontWeight.Bold,
                color = GreenText,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ExpensesFinanceResume(
    modifier: Modifier = Modifier,
    title: String,
    financeData: ExpenseFinanceData,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = PrimaryCardBackground),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.weight(1f)) {
                RowWithMidTitleAndDescription(
                    "Cantidad de gastos:",
                    financeData.expensesQuantity.toString()
                )
                RowWithMidTitleAndDescription("Total:", financeData.totalAmount.toPrice())
                RowWithMidTitleAndDescription(
                    "Mayor gasto:",
                    "${financeData.maxExpense?.description ?: "No hay registro"}: ${financeData.maxExpense?.amount?.toPrice() ?: ""}"
                )
                RowWithMidTitleAndDescription("Con respecto al mes anterior:", 15L.toPercentAdd())
            }
        }
    }
}
