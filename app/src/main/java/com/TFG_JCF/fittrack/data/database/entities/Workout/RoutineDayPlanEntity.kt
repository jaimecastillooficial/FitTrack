package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_day_plans",
    foreignKeys = [
        ForeignKey(
            entity = RoutineWeekEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineWeekId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["routineWeekId"]),
        Index(value = ["routineWeekId", "dayOfWeek"], unique = true)
    ]
)
data class RoutineDayPlanEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val routineWeekId: Long,

    // 1..7 (Lunes..Domingo o lo que decidas)
    val dayOfWeek: Int,

    // "PUSH", "PULL", "LEGS", etc.
    val dayName: String,

    val orderIndex: Int? = null
)
