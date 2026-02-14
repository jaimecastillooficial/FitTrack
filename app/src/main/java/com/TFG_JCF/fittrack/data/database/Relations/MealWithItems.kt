package com.TFG_JCF.fittrack.data.database.Relations

import androidx.room.Embedded
import androidx.room.Relation
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealItemEntity

data class MealWithItems (
    @Embedded val meal: MealEntity,

    @Relation(
        entity = MealItemEntity::class,
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val items: List<MealItemEntity>
    )