package org.example.project.domain.models.supplier

import org.example.project.data.db.entities.SupplierEntity
import org.example.project.ui.models.SupplierPresentation

data class Supplier(
    val id:Int = 0,
    val name: String,
    val mail: String,
    val phoneNumber:Long,
    val webpage:String,
    val address:String,
    val productsOffered: String
) {
    fun toEntity(): SupplierEntity {
        return SupplierEntity(
            id = id,
            name = name,
            mail = mail,
            webpage = webpage,
            phoneNumber = phoneNumber,
            address = address,
            productsOffered = productsOffered
        )
    }

    fun toPresentation(): SupplierPresentation {

        val productsList = productsOffered.split(",").map { word ->
            word.trim().replaceFirstChar { char ->
                char.uppercaseChar()
            }
        }.filter { it.isNotBlank() }

        return SupplierPresentation(
            id = id,
            name = name,
            mail = mail,
            phoneNumber = phoneNumber,
            webpage = webpage,
            address = address,
            productsOffered = productsList
        )
    }
}
