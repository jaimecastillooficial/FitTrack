package com.TFG_JCF.fittrack.data.database

import androidx.room.TypeConverter
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType

class Converters {

    @TypeConverter
    fun fromMealType(value: MealType): String {
        return value.name
    }

    @TypeConverter
    fun toMealType(value: String): MealType {
        return MealType.valueOf(value)
    }
}