package org.example.project.domain.usecases.sells

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import org.example.project.data.db.daos.SellRepository
import org.example.project.domain.models.sell.Sell
import org.example.project.domain.usecases.utils.GetCurrentDate

enum class SellFilterLabel(val etiquette: String) {
    TODAY("Hoy"), WEEK("Esta semana"), MONTH("Este mes"), ALL("Todo")
}

class GetSells(
    private val sellRepository: SellRepository,
    private val getCurrentDate: GetCurrentDate,
) {
    operator fun invoke(query: String, filter: SellFilterLabel): Flow<List<Sell>> {
        val sells = if(query.isNotBlank()) {
            sellRepository.getSellsByQuery(query)
        } else {
            sellRepository.getAllSells()
        }
        val today = getCurrentDate()

        return when (filter) {
            SellFilterLabel.TODAY -> sells.map { list ->
                list.filter { sell ->
                    sell.date?.let { date ->
                        date == today
                    } == true
                }
            }

            SellFilterLabel.WEEK -> sells.map { list ->
                list.filter { sell ->
                    sell.date?.let { date ->
                        date >= today.minus(7, DateTimeUnit.DAY)
                    } == true
                }
            }

            SellFilterLabel.MONTH -> sells.map { list ->
                list.filter { sell ->
                    sell.date?.let { date ->
                        date.year == today.year &&
                                date.month == today.month
                    } == true
                }
            }
            SellFilterLabel.ALL -> sells
        }

    }


}