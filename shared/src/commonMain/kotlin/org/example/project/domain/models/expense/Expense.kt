package org.example.project.domain.models.expense

import kotlinx.datetime.LocalDate
import org.example.project.data.db.entities.ExpenseEntity

data class Expense(
    val id:Int = 0,
    val description:String,
    val amount:Long,
    val date: LocalDate? = null,
) {
    fun toEntity(): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            description = description,
            amount = amount,
            date = date!!
        )
    }
}
