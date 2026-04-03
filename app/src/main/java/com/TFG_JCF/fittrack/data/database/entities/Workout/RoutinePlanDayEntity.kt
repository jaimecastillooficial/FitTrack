package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_plan_days",
    foreignKeys = [
        ForeignKey(
            entity = RoutineDayPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayPlanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["dayPlanId"]),
        Index(value = ["dayPlanId", "dayOfWeek"], unique = true)
    ]
)
data class RoutinePlanDayEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val dayPlanId: Long,

    // 1..7 -> lunes..domingo
    val dayOfWeek: Int
)