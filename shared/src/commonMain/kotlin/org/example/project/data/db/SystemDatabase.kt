package org.example.project.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.example.project.data.db.daos.ClientDao
import org.example.project.data.db.entities.ClientEntity


const val DATABASE_NAME = "ss_app_database.db"

expect object SystemCTor : RoomDatabaseConstructor<SystemDatabase>

@Database(entities = [ClientEntity::class], version = 1)
@ConstructedBy(SystemCTor::class)
abstract class SystemDatabase:RoomDatabase() {
    abstract fun clientDao():ClientDao
}