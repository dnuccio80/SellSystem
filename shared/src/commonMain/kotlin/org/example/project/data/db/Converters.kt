package org.example.project.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toList(json: String): List<String> {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}
