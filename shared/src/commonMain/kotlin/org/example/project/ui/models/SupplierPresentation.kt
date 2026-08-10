package org.example.project.ui.models

data  class SupplierPresentation(
    val id:Int,
    val name: String,
    val mail: String,
    val phoneNumber:Long,
    val webpage:String,
    val address:String,
    val productsOffered: List<String>
)