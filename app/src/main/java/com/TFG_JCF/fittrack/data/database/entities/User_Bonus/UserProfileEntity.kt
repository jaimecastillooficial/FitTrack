package com.TFG_JCF.fittrack.data.database.entities.User_Bonus

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(

    @PrimaryKey
    val uid: String,   // UID de Firebase

    val name: String?,

    val heightCm: Int,

    val goalType: String,   // Volumen / Deficion / Mantenimiento

    val activityLevel: String,  // BAJO / MEDIO / ALTO

    val createdAt: Long
)
