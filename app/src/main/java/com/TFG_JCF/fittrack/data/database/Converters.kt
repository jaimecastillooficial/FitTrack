package com.TFG_JCF.fittrack.data.database

import androidx.room.TypeConverter
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType
import com.TFG_JCF.fittrack.data.database.entities.Workout.MovementPattern

class Converters {

    @TypeConverter
    fun fromMealType(value: MealType): String {
        return value.name
    }

    @TypeConverter
    fun toMealType(value: String): MealType {
        return MealType.valueOf(value)
    }
    @TypeConverter
    fun fromMovementPattern(value: MovementPattern?): String? {
        return value?.name
    }

    @TypeConverter
    fun toMovementPattern(value: String?): MovementPattern? {
        return value?.let { MovementPattern.valueOf(it) }
    }
}