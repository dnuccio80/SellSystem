package org.example.project.domain.usecases.sells

import org.example.project.data.db.daos.SellRepository
import org.example.project.domain.models.sell.SellError
import org.example.project.domain.usecases.newsell.PaymentMethod
import org.example.project.ui.models.SellPresentation

class CreateNewSell(private val sellRepository: SellRepository) {

    operator fun invoke(sellPresentation: SellPresentation) {
        when {
            sellPresentation.isUsualClient && sellPresentation.clientName.isBlank() -> throw SellError.UsualClientButNotSelected
            sellPresentation.productQuantityList.isEmpty() -> throw SellError.NoItems
            sellPresentation.paymentMethod == PaymentMethod.CURRENT_ACCOUNT && sellPresentation.clientName.isBlank() ->  throw SellError.CurrentAccountButNotSelected
        }
    }

}