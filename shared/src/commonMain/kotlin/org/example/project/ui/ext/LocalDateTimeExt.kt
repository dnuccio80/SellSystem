package org.example.project.ui.ext

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

fun LocalDateTime.formatToDisplay(): String {
    return "${day.toString().padStart(2, '0')}/" +
            "${month.number.toString().padStart(2, '0')}/" +
            "$year - " +
            "${hour.toString().padStart(2, '0')}:" +
            minute.toString().padStart(2, '0')
}