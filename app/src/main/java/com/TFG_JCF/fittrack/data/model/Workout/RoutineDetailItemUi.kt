package com.TFG_JCF.fittrack.data.model.Workout

data class RoutineDetailItemUi(
    val title: String,
    val selectedDays: List<Int>,
    val sourceDayPlanIds: List<Long>
)