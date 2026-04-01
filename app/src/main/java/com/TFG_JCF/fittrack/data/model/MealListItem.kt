package com.TFG_JCF.fittrack.data.model

import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType

sealed class MealListItem {

    data class Header(
        val mealType: MealType
    ) : MealListItem()

    data class FoodItem(
        val mealItemId: Long,
        val name: String,
        val calories: Int,
        val grams: Int
    ) : MealListItem()

}