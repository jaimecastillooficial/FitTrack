package com.TFG_JCF.fittrack.data.utils


import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.ActivityLevel
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.Gender
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.GoalType
import com.TFG_JCF.fittrack.data.model.CalorieRecomendation
import kotlin.math.roundToInt

object RecommendationCalculator {

    fun calculateRecommendation(
        gender: Gender,
        age: Int,
        height: Int,
        weight: Float,
        activityLevel: ActivityLevel,
        goalType: GoalType
    ): CalorieRecomendation {

        val bmr = calculateBmr(
            gender = gender,
            weight = weight,
            height = height,
            age = age
        )

        val maintenanceCalories = (bmr * getActivityMultiplier(activityLevel)).roundToInt()

        val recommendedCalories = when (goalType) {
            GoalType.LOSE_WEIGHT -> maintenanceCalories - 300
            GoalType.MAINTAIN_WEIGHT -> maintenanceCalories
            GoalType.GAIN_WEIGHT -> maintenanceCalories + 300
        }

        val recommendedTargetWeight = calculateRecommendedTargetWeight(
            heightCm = height,
            currentWeight = weight,
            goalType = goalType
        )

        return CalorieRecomendation(
            recommendedCalories = recommendedCalories,
            recommendedTargetWeight = recommendedTargetWeight
        )
    }

    // Tasa metabólica basal
    private fun calculateBmr(
        gender: Gender,
        weight: Float,
        height: Int,
        age: Int
    ): Float {
        return when (gender) {
            Gender.MALE -> (10f * weight) + (6.25f * height) - (5f * age) + 5f
            Gender.FEMALE -> (10f * weight) + (6.25f * height) - (5f * age) - 161f
        }
    }

    // Multiplicador según actividad
    private fun getActivityMultiplier(activityLevel: ActivityLevel): Float {
        return when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2f
            ActivityLevel.LIGHT -> 1.375f
            ActivityLevel.MODERATE -> 1.55f
            ActivityLevel.HIGH -> 1.725f
            ActivityLevel.VERY_HIGH -> 1.9f
        }
    }

    // Peso objetivo recomendado basado en rango saludable
    private fun calculateRecommendedTargetWeight(
        heightCm: Int,
        currentWeight: Float,
        goalType: GoalType
    ): Float {
        val heightM = heightCm / 100f

        val minHealthyWeight = 18.5f * heightM * heightM
        val maxHealthyWeight = 24.9f * heightM * heightM
        val middleHealthyWeight = 22f * heightM * heightM

        val target = when (goalType) {
            GoalType.LOSE_WEIGHT -> {
                if (currentWeight > maxHealthyWeight) {
                    maxHealthyWeight
                } else {
                    maxOf(currentWeight - 2f, minHealthyWeight)
                }
            }

            GoalType.MAINTAIN_WEIGHT -> {
                if (currentWeight in minHealthyWeight..maxHealthyWeight) {
                    currentWeight
                } else {
                    middleHealthyWeight
                }
            }

            GoalType.GAIN_WEIGHT -> {
                if (currentWeight < minHealthyWeight) {
                    minHealthyWeight
                } else {
                    minOf(currentWeight + 2f, maxHealthyWeight)
                }
            }
        }

        return (target * 10).roundToInt() / 10f
    }
}