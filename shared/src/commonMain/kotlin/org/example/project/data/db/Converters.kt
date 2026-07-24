package org.example.project.data.db

import androidx.room.TypeConverter
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
}
