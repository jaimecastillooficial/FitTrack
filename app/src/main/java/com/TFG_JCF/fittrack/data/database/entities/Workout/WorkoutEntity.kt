package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["uid"],
            childColumns = ["userUid"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoutineDayPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["basedOnDayPlanId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["userUid"]),
        Index(value = ["basedOnDayPlanId"])
    ]
)
data class WorkoutEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userUid: String,

    val dateTimeStart: Long,

    val dateTimeEnd: Long? = null,

    val dayName: String? = null,

    // nullable: puede ser un entreno creado sin plantilla
    val basedOnDayPlanId: Long? = null
)
