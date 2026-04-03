package com.TFG_JCF.fittrack.data.utils

import com.TFG_JCF.fittrack.data.database.Relations.Diet.MealWithItemsAndFoods
import com.TFG_JCF.fittrack.data.model.Diet.DailyNutritionSummary
import com.TFG_JCF.fittrack.data.model.Diet.MacroTargets
import kotlin.math.roundToInt

object NutritionCalculator {

    fun calculateDailySummary(meals: List<MealWithItemsAndFoods>): DailyNutritionSummary {
        var totalCalories = 0
        var totalProtein = 0f
        var totalCarbs = 0f
        var totalFats = 0f

        meals.forEach { meal ->
            meal.items.forEach { mealItemWithFood ->
                val food = mealItemWithFood.food
                val grams = mealItemWithFood.item.grams

                totalCalories += ((food.kcalPer100g * grams) / 100f).roundToInt()
                totalProtein += (food.proteinPer100g * grams) / 100f
                totalCarbs += (food.carbsPer100g * grams) / 100f
                totalFats += (food.fatPer100g * grams) / 100f
            }
        }

        return DailyNutritionSummary(
            caloriesConsumed = totalCalories,
            proteinConsumed = round1(totalProtein),
            carbsConsumed = round1(totalCarbs),
            fatsConsumed = round1(totalFats)
        )
    }

    fun calculateMacroTargets(caloriesGoal: Int): MacroTargets {
        val proteinGoal = ((caloriesGoal * 0.30f) / 4f).roundToInt()
        val carbsGoal = ((caloriesGoal * 0.40f) / 4f).roundToInt()
        val fatsGoal = ((caloriesGoal * 0.30f) / 9f).roundToInt()

        return MacroTargets(
            proteinGoal = proteinGoal,
            carbsGoal = carbsGoal,
            fatsGoal = fatsGoal
        )
    }

    private fun round1(value: Float): Float {
        return (value * 10).roundToInt() / 10f
    }
}