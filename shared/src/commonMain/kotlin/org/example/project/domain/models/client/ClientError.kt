package org.example.project.domain.models.client

import org.example.project.domain.models.product.ProductError

sealed class ClientError(val msg:String): Exception () {
    data object EmptyName: ClientError("Debes ingresar el nombre del cliente")
    data object EmptyPhone: ClientError("Debes ingresar el número de teléfono")
    data object NoAddress: ClientError("Debes ingresar la dirección del cliente")
    data object NoCity: ClientError("Debes ingresar la ciudad del cliente")
    data object NoProvince: ClientError("Debes ingresar la provincia del cliente")
}