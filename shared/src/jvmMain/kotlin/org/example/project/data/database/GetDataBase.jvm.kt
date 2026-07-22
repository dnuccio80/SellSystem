package org.example.project.data.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.example.project.data.db.DATABASE_NAME
import org.example.project.data.db.SystemDatabase
import java.io.File

fun getDataBase(): SystemDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DATABASE_NAME)
    return Room.databaseBuilder<SystemDatabase>(name = dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(
            dropAllTables = true
        )
        .build()

}