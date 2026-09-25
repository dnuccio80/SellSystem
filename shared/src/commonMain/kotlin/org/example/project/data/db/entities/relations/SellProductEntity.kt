package org.example.project.data.db.entities.relations

import androidx.room.Entity

@Entity(primaryKeys = ["sellId", "productId"])
data class SellProductEntity(
    val sellId:Int,
    val productId:Int,
    val quantity:Int
) {

}
