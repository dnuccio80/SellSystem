package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.ui.ScreenContainer
import org.example.project.ui.SimpleGenericHeader

class LoyaltySystemScreen: Screen {
    @Composable
    override fun Content() {
        ScreenContainer {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SimpleGenericHeader(
                    title = "Sistema de lealtad",
                    description = "Programa de lealtad para clientes usuales"
                )
            }
        }
    }
}