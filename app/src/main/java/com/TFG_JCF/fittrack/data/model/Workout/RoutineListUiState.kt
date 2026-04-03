package com.TFG_JCF.fittrack.data.model.Workout

data class RoutineListUiState(
    val routines: List<RoutineListItemUi> = emptyList(),
    val message: String = "No tienes rutinas todavía"
)