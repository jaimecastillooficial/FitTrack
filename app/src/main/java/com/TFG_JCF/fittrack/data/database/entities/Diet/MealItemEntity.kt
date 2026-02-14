package com.TFG_JCF.fittrack.data.database.entities.Diet

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_items",
    foreignKeys = [
        ForeignKey(
            entity = MealEntity::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["mealId"]),
        Index(value = ["foodId"])
    ]
)
data class MealItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val mealId: Long,

    val foodId: Long,

    val grams: Float
)
