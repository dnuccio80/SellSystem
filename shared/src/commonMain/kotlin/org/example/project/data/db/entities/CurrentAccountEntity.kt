package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.currentaccount.CurrentAccount

@Entity
data class CurrentAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val clientId:Int,
    val amount:Long
) {
    fun toDomain(): CurrentAccount {
        return CurrentAccount(
            id = id,
            clientId = clientId,
            amount = amount
        )
    }
}
