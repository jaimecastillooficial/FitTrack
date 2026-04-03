package com.TFG_JCF.fittrack.data.model.Diet

import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType

data class FoodMenuUiState(
    val foodId: Long = 0L,
    val foodName: String = "",
    val kcalPer100g: Int = 0,
    val proteinPer100g: Float = 0f,
    val carbsPer100g: Float = 0f,
    val fatPer100g: Float = 0f,
    val grams: Float = 100f,
    val mealType: MealType = MealType.DESAYUNO,
    val calories: Int = 0,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f
)