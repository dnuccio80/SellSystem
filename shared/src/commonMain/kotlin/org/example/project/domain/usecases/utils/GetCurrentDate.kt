package org.example.project.domain.usecases.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class GetCurrentDate {
    operator fun invoke(): LocalDate {
        return Clock.System.todayIn(TimeZone.of("America/Argentina/Buenos_Aires"))
    }
}