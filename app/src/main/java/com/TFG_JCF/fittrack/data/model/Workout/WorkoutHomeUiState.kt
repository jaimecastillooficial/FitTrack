package com.TFG_JCF.fittrack.data.model.Workout

data class WorkoutHomeUiState(
    val activeRoutineName: String = "Ninguna",
    val todayWorkoutName: String = "-",
    val message: String = "Crea o activa una rutina para empezar",
    val canStartWorkout: Boolean = false
)