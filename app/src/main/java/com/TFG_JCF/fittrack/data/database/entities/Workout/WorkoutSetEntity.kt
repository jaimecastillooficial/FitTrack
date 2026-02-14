package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workoutExerciseId"])
    ]
)
data class WorkoutSetEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val workoutExerciseId: Long,

    val setNumber: Int,

    val reps: Int? = null,

    val weightKg: Float? = null,

    val rir: Int? = null,

    val isWarmup: Boolean? = null
)
