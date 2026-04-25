package com.TFG_JCF.fittrack.data.database.entities.Workout

enum class MovementPattern {
    PUSH,
    PULL,
    LEGS,
    CORE
}

fun MovementPattern.toDisplayName(): String {
    return when (this) {
        MovementPattern.PUSH -> "Empuje"
        MovementPattern.PULL -> "Tirón"
        MovementPattern.LEGS -> "Pierna"
        MovementPattern.CORE -> "Core"
    }
}