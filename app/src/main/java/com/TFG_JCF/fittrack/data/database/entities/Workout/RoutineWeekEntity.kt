package com.TFG_JCF.fittrack.data.database.entities.Workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity

@Entity(
    tableName = "routine_weeks",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["uid"],
            childColumns = ["userUid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userUid"])
    ]
)
data class RoutineWeekEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userUid: String,

    val name: String,

    val isActive: Boolean = false,

    val createdAt: Long
)
