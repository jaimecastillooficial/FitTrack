package com.TFG_JCF.fittrack.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "foods",
    indices = [
        Index(value = ["name"])
    ]
)
data class FoodEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val kcalPer100g: Int,

    val proteinPer100g: Float,

    val carbsPer100g: Float,

    val fatPer100g: Float,

    val isPublic: Boolean,

    val createdByUid: String?   // null = alimento predefinido
)
