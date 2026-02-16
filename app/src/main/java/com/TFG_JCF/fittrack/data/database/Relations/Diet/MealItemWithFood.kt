package com.TFG_JCF.fittrack.data.database.Relations.Diet


import androidx.room.Embedded
import androidx.room.Relation
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealItemEntity

data class MealItemWithFood(
    @Embedded val item: MealItemEntity,

    @Relation(
        parentColumn = "foodId",
        entityColumn = "id"
    )
    val food: FoodEntity
)
