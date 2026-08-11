package org.example.project.domain.models.currentaccount

import org.example.project.data.db.entities.CurrentAccountEntity

data class CurrentAccount(
    val id:Int = 0,
    val clientId:Int,
    val amount:Long
) {
    fun toEntity(): CurrentAccountEntity {
        return CurrentAccountEntity(
            id = id,
            clientId = clientId,
            amount = amount
        )
    }
}
