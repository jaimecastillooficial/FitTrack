package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercises",
    indices = [
        Index(value = ["name"]),
        Index(value = ["createdByUid"])
    ]
)
data class ExerciseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val muscleGroup: String? = null,

    val equipment: String? = null,

    // predefinidos: true, creados por usuario: false
    val isPublic: Boolean,

    // null = ejercicio predefinido
    val createdByUid: String? = null
)
