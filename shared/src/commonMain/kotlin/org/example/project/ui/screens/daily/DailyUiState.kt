package org.example.project.ui.screens.daily

import org.example.project.domain.models.daily.CleanDailyData
import org.example.project.domain.models.daily.DailyData

data class DailyUiState (
    val isOpen: Boolean = false,
    val dailyData: DailyData = CleanDailyData().getCleanDailyData()
)
