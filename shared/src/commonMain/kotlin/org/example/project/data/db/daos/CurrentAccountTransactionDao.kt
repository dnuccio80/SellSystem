package org.example.project.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.example.project.data.db.entities.CurrentAccountTransactionEntity
import org.example.project.data.db.entities.TransactionType

@Dao
interface CurrentAccountTransactionDao {

    @Query("SELECT * FROM CurrentAccountTransactionEntity WHERE clientId = :clientId ORDER BY date, id DESC")
    fun getTransactions(clientId:Int): Flow<List<CurrentAccountTransactionEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addTransaction(transaction: CurrentAccountTransactionEntity):Long

    @Query("SELECT * FROM CurrentAccountTransactionEntity WHERE id = :id")
    suspend fun getTransactionById(id:Int): CurrentAccountTransactionEntity

    @Transaction
    suspend fun insertTransaction(
        transaction: CurrentAccountTransactionEntity
    ):Long {
        val transactionId = addTransaction(transaction)

        when (transaction.type) {
            TransactionType.PURCHASE -> {
                increaseAmount(
                    clientId = transaction.clientId,
                    amount = transaction.amount
                )
            }

            TransactionType.PAYMENT -> {
                decreaseAmount(
                    clientId = transaction.clientId,
                    amount = transaction.amount
                )
            }

            else -> {}
        }
        return transactionId
    }

    @Transaction
    suspend fun deleteTransaction(transaction: CurrentAccountTransactionEntity) {
        deleteTransactionFromList(transaction.id)

        when (transaction.type) {
            TransactionType.PURCHASE -> {
                decreaseAmount(
                    clientId = transaction.clientId,
                    amount = transaction.amount
                )
            }

            TransactionType.PAYMENT -> {
                increaseAmount(
                    clientId = transaction.clientId,
                    amount = transaction.amount
                )
            }

            else -> {}
        }
    }

    @Query("DELETE FROM currentaccounttransactionentity WHERE id = :id")
    suspend fun deleteTransactionFromList(id:Int)

    @Query("UPDATE currentaccountentity SET amount = amount - :amount WHERE clientId = :clientId")
    suspend fun decreaseAmount(clientId:Int, amount:Long)

    @Query("UPDATE currentaccountentity SET amount = amount + :amount WHERE clientId = :clientId")
    suspend fun increaseAmount(clientId:Int, amount:Long)


}