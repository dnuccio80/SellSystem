package org.example.project.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import org.example.project.domain.models.expense.Expense

@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val description:String,
    val amount:Long,
    val date: LocalDate
) {
    fun toDomain(): Expense {
        return Expense(
            id = id,
            description = description,
            amount = amount,
            date = date
        )
    }
}
