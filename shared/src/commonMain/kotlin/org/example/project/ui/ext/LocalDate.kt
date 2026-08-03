package org.example.project.ui.ext

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun LocalDate.formatToDisplay(): String {
    return "${day.toString().padStart(2, '0')}/" +
            "${month.number.toString().padStart(2, '0')}/" +
            year
}