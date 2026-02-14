package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_day_exercises",
    foreignKeys = [
        ForeignKey(
            entity = RoutineDayPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayPlanId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["dayPlanId"]),
        Index(value = ["exerciseId"])
    ]
)
data class RoutineDayExerciseEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val dayPlanId: Long,

    val exerciseId: Long,

    val orderIndex: Int,

    // opcionales (el usuario puede no planificar esto)
    val setsPlanned: Int? = null,
    val repsPlanned: Int? = null,
    val rirPlanned: Int? = null
)
