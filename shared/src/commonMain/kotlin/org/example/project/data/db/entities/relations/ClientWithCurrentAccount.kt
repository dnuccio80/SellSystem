package org.example.project.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.example.project.data.db.entities.ClientEntity
import org.example.project.data.db.entities.CurrentAccountEntity
import org.example.project.domain.models.client.Client
import org.example.project.domain.models.currentaccount.CurrentAccount

data class ClientWithCurrentAccount(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "clientId",
    )
    val currentAccount: CurrentAccountEntity?
)
