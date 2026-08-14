package org.example.project.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.example.project.data.db.entities.ClientEntity
import org.example.project.data.db.entities.CurrentAccountTransactionEntity

data class ClientWithTransactionsData(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "clientId"
    )
    val transactions: List<CurrentAccountTransactionEntity>
)
