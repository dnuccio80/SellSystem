package org.example.project.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import org.example.project.data.db.entities.ClientEntity
import org.example.project.data.db.entities.CurrentAccountEntity
import org.example.project.domain.models.currentaccount.ClientWithCurrentAccount

data class ClientWithCurrentAccountEntity(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "clientId",
    )
    val currentAccount: CurrentAccountEntity?
) {
    fun toDomain(): ClientWithCurrentAccount {
        return ClientWithCurrentAccount(
            client = client.toDomain(),
            currentAccount = currentAccount?.toDomain()
        )
    }
}
