package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.example.project.domain.models.supplier.Supplier

@Entity
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val mail: String,
    val webpage:String,
    val phoneNumber:Long,
    val address:String,
    val productsOffered:String
) {
    fun toDomain(): Supplier {
        return Supplier(
            id = id,
            name = name,
            mail = mail,
            webpage = webpage,
            phoneNumber = phoneNumber,
            address = address,
            productsOffered = productsOffered
        )
    }
}
