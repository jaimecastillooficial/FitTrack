package com.TFG_JCF.fittrack.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weight_entries",
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
        Index(value = ["userUid", "date"], unique = true)
    ]
)
data class WeightEntryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userUid: String,

    val date: String,

    val weightKg: Float
)
