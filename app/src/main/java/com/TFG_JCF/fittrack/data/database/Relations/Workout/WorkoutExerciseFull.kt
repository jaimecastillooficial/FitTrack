package com.TFG_JCF.fittrack.data.database.Relations.Workout

import androidx.room.Embedded
import androidx.room.Relation
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutSetEntity

data class WorkoutExerciseFull(
    @Embedded
    val workoutExercise: WorkoutExerciseEntity,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "id"
    )
    val exercise: ExerciseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<WorkoutSetEntity>
)