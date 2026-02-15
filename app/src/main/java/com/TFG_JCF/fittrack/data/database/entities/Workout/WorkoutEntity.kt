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
        )
    ],
    //Optimiza las consultas que haga ROOM como "SELECT * FROM workouts WHERE userUid = ?" y "SELECT * FROM workouts WHERE userUid = ? AND date = ?"
    indices = [
        Index(value = ["userUid", "date"], unique = true)
    ]
)
data class WorkoutEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userUid: String,

    val date: String,

    val dayName: String,

)
