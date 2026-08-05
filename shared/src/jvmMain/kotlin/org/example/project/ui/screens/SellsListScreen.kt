package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.ui.GenericHeaderWithButtonAndSearch
import org.example.project.ui.ScreenContainer

class SellsListScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GenericHeaderWithButtonAndSearch(
                    title = "Ventas",
                    description = "Listado de ventas históricas",
                    buttonText = "Nueva venta"
                ) { navigator?.push(NewSellScreen()) }
            }

        }
    }
}