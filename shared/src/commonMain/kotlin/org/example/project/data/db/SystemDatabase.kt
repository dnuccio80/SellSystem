package org.example.project.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import org.example.project.data.db.daos.ClientDao
import org.example.project.data.db.daos.CurrentAccountDao
import org.example.project.data.db.daos.CurrentAccountTransactionDao
import org.example.project.data.db.daos.ExpensesDao
import org.example.project.data.db.daos.ProductCategoryDao
import org.example.project.data.db.daos.ProductDao
import org.example.project.data.db.daos.ProductVariantDao
import org.example.project.data.db.daos.SupplierDao
import org.example.project.data.db.entities.ClientEntity
import org.example.project.data.db.entities.CurrentAccountEntity
import org.example.project.data.db.entities.CurrentAccountTransactionEntity
import org.example.project.data.db.entities.ExpenseEntity
import org.example.project.data.db.entities.ProductCategoryEntity
import org.example.project.data.db.entities.ProductEntity
import org.example.project.data.db.entities.ProductVariantEntity
import org.example.project.data.db.entities.SupplierEntity


const val DATABASE_NAME = "ss_app_database.db"

expect object SystemCTor : RoomDatabaseConstructor<SystemDatabase>

@Database(
    entities = [ClientEntity::class, ProductVariantEntity::class, ProductEntity::class, ProductCategoryEntity::class, ExpenseEntity::class, SupplierEntity::class, CurrentAccountEntity::class, CurrentAccountTransactionEntity::class],
    version = 16
)
@ConstructedBy(SystemCTor::class)
@TypeConverters(Converters::class)
abstract class SystemDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun productVariantDao(): ProductVariantDao
    abstract fun productDao(): ProductDao
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun expenseDao(): ExpensesDao
    abstract fun supplierDao(): SupplierDao
    abstract fun currentAccountDao(): CurrentAccountDao
    abstract fun currentAccountTransactionDao(): CurrentAccountTransactionDao
}