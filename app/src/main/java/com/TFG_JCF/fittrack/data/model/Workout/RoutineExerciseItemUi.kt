package com.TFG_JCF.fittrack.data.model.Workout

import com.TFG_JCF.fittrack.data.database.entities.Workout.MovementPattern

data class RoutineExerciseItemUi(
    val exerciseId: Long,
    val name: String,
    val muscleGroup: String?,
    val movementPattern: MovementPattern?
)
