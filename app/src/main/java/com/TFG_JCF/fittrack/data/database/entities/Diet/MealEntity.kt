package com.TFG_JCF.fittrack.data.database.entities.Diet

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity

@Entity(
    tableName = "meals",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["uid"],
            childColumns = ["userUid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userUid"]),
        Index(value = ["userUid", "date", "type"], unique = true)
    ]
)
data class MealEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userUid: String,

    val date: String,  // YYYY-MM-DD

    val type: String,  // DESAYUNO / COMIDA / CENA / SNACK

    val name: String? = null
)
