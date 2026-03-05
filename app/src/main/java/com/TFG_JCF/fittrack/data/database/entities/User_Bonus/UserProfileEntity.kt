package com.TFG_JCF.fittrack.data.database.entities.User_Bonus

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(

    @PrimaryKey
    val uid: String,   // UID de Firebase

    val name: String,

    val gender: Gender,

    val age: Int,

    val heightCm: Int,

    val currentWeight: Float,

    val targetWeight: Float? = null,

    val goalType: GoalType,   // Volumen / Deficion / Mantenimiento

    val activityLevel: ActivityLevel,  // BAJO / MEDIO / ALTO

    val dailyCaloriesGoal: Int? = null,

    val createdAt: Long = System.currentTimeMillis()

)
