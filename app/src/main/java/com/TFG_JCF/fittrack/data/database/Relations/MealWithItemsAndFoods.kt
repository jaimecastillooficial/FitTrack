package com.TFG_JCF.fittrack.data.database.Relations

import com.TFG_JCF.fittrack.data.database.entities.Diet.MealEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealItemEntity

import androidx.room.Embedded
import androidx.room.Relation

data class MealWithItemsAndFoods(
    @Embedded val meal: MealEntity,

    @Relation(
        entity = MealItemEntity::class,
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val items: List<MealItemWithFood>
)
