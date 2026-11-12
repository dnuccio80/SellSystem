package org.example.project.ui.screens.loyalty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SimpleGenericHeader
import org.example.project.ui.screens.expenses.ExpensesUiState.Success
import org.koin.compose.viewmodel.koinViewModel

class LoyaltySystemScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel = koinViewModel<LoyaltyViewModel>()
        val loyaltyAccounts by viewModel.loyaltyAccounts.collectAsStateWithLifecycle()
        val query by viewModel.query.collectAsStateWithLifecycle()

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Sistema de lealtad",
                    description = "Programa de lealtad para clientes usuales",
                    buttonText = "Gestionar puntajes",
                    searchValue = query,
                    onSearchValueChange = { viewModel.updateQuery(it) },
                    onDeleteQuerySearch = { viewModel.updateQuery("") }
                ) {

                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if(loyaltyAccounts.isNotEmpty()) {
                        items(loyaltyAccounts) {
                            LoyaltyAccountItem()
                        }
                    } else {
                        item {
                            Text(
                                "No hay cuentas de fidelidad en la búsqueda",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoyaltyAccountItem() {
    Text("djdjk")
}