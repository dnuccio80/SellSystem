package org.example.project.domain.models.pendingorders

import org.example.project.domain.models.product.ProductError

sealed class PendingOrderError(val msg: String): Exception() {
    data object NoClientName: PendingOrderError("Debes agregar el nombre del cliente")
    data object NoPhone: PendingOrderError("Debes agregar el teléfono del cliente")
    data object NoAddress: PendingOrderError("Debes agregar una dirección para el cliente")
    data object EmptyDescription: PendingOrderError("Debes agregar una descripción de la orden")

}
