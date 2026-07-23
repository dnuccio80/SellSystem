package org.example.project.domain.models

import org.example.project.data.db.entities.ClientEntity

data class Client(
    val id:Int = 0,
    val fullName:String,
    val phoneNumber:Long,
    val address:String,
    val birthday:String,
    val notes:String,
    val loyaltyPoints:Int = 0,
    val hasCurrentAccount: Boolean = false
) {
    fun toEntity(): ClientEntity {
        return ClientEntity(
            id = id,
            fullName = fullName,
            phoneNumber = phoneNumber,
            address = address,
            birthday = birthday,
            notes = notes,
            loyaltyPoints = loyaltyPoints,
            hasCurrentAccount = hasCurrentAccount
        )
    }
}
