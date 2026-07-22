package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.Client

@Entity
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val fullName:String,
    val phoneNumber:Long,
    val address:String,
    val birthday:String,
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
            birthday = birthday,
            notes = notes,
            loyaltyPoints = loyaltyPoints,
            hasCurrentAccount = hasCurrentAccount
        )
    }
}
