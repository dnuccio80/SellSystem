package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.project.domain.models.client.Client

@Entity
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val fullName:String,
    val phoneNumber:Long,
    val address:String,
    val city:String,
    val province:String,
    val birthday: LocalDate?,
    val notes:String,
    val loyaltyPoints:Int,
    val hasCurrentAccount: Boolean
) {
    fun toDomain(): Client {
        return Client(
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
