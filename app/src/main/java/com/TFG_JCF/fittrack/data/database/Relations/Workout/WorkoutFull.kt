package com.TFG_JCF.fittrack.data.database.Relations.Workout

import androidx.room.Embedded
import androidx.room.Relation
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutExerciseEntity

data class WorkoutFull(

    @Embedded
    val workout: WorkoutEntity,

    @Relation(
        //Tabla intermedia entre WorkoutEntity("Push") y ExerciseEntity("Fondos")
        entity = WorkoutExerciseEntity::class,
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val exercises: List<WorkoutExerciseFull>

)