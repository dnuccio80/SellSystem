package org.example.project.domain.models.client

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.ClientEntity

data class Client(
    val id:Int = 0,
    val fullName:String,
    val phoneNumber:Long,
    val address:String,
    val city:String,
    val province:String,
    val birthday: LocalDate? = null,
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
            city = city,
            province = province,
            birthday = birthday,
            notes = notes,
            loyaltyPoints = loyaltyPoints,
            hasCurrentAccount = hasCurrentAccount
        )
    }
}