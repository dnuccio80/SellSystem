package org.example.project.domain.usecases.promotions

import org.example.project.domain.models.promotions.PromotionCategory.*
import org.example.project.domain.models.promotions.PromotionType
import org.example.project.domain.models.promotions.PromotionsError
import org.example.project.domain.repositories.PromotionRepository
import org.example.project.ui.models.PromotionPresentation

class AddPromotion(private val repository: PromotionRepository) {

    suspend operator fun invoke(promotion: PromotionPresentation) {

        when(promotion.promotionType) {
            PromotionType.BUY_X_PAY_Y ->  {
                if(promotion.buyX == 0 || promotion.buyX == null) throw PromotionsError.NoBuyAmount
                else if(promotion.payY == 0 || promotion.payY == null) throw PromotionsError.NoPayAmount

                if(promotion.payY > promotion.buyX) throw PromotionsError.PayMoreThanBuy
            }
            PromotionType.PERCENT -> {
                if(promotion.percent == null || promotion.percent <= 0) throw PromotionsError.NoPercentAmount
            }
        }

        when(promotion.promoteBy) {
            BRAND -> { if(promotion.brandSelected.isNullOrBlank()) throw PromotionsError.NoBrandsAdded }
            CATEGORY -> { if(promotion.categoryListSelected.isNullOrEmpty()) throw PromotionsError.NoCategoriesAdded }
            SPECIFIC -> { if(promotion.specificProducts.isNullOrEmpty()) throw PromotionsError.NoSpecificProductsAdded }
        }

        if(promotion.hasDate) {
            if(promotion.hasDateInit && promotion.dateInit == null) throw PromotionsError.NoPromoDataInit
            if(promotion.hasDateEnd && promotion.dateEnd == null) throw PromotionsError.NoPromoDataEnd
            if(promotion.hasDateInit && promotion.hasDateEnd) {
                if(promotion.dateInit!! > promotion.dateEnd!!) { throw PromotionsError.IncompatiblePromoDateInitEnd }
            }
            if(!promotion.hasDateInit && !promotion.hasDateEnd) throw PromotionsError.NoPromoDataInitAndEndWithHasDateSelected
        }


    }



}