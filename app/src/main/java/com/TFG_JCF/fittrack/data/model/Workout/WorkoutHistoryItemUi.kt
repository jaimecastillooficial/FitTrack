package com.TFG_JCF.fittrack.data.model.Workout

data class WorkoutHistoryItemUi(
    val id: Long,
    val date: String,
    val dayName: String,
    val exerciseCount: Int,
    val setCount: Int,
    val detailText: String
)
