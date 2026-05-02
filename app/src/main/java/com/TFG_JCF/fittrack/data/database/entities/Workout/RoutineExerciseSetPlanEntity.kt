package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_exercise_set_plans",
    foreignKeys = [
        ForeignKey(
            entity = RoutineDayExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineDayExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["routineDayExerciseId"]),
        Index(value = ["routineDayExerciseId", "setNumber"], unique = true)
    ]
)
data class RoutineExerciseSetPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val routineDayExerciseId: Long,
    val setNumber: Int,
    val plannedWeightKg: Float? = null,
    val plannedReps: Int,
    val plannedRir: Int? = null
)