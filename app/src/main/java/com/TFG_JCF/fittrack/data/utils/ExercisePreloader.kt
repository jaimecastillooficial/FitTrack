package com.TFG_JCF.fittrack.data.utils

import com.TFG_JCF.fittrack.data.database.dao.Workout.ExerciseDao
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.MovementPattern
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object ExercisePreloader {

    fun defaultExercises(): List<ExerciseEntity> {
        return listOf(
            ExerciseEntity("Press banca", "Pecho", MovementPattern.PUSH),
            ExerciseEntity("Press inclinado con mancuernas", "Pecho", MovementPattern.PUSH),
            ExerciseEntity("Fondos en paralelas", "Pecho / Tríceps", MovementPattern.PUSH),
            ExerciseEntity("Aperturas con mancuernas", "Pecho", MovementPattern.PUSH),
            ExerciseEntity("Press militar", "Hombro", MovementPattern.PUSH),
            ExerciseEntity("Elevaciones laterales", "Hombro", MovementPattern.PUSH),
            ExerciseEntity("Extensión de tríceps en polea", "Tríceps", MovementPattern.PUSH),
            ExerciseEntity("Extensión unilateral de tríceps", "Tríceps", MovementPattern.PUSH),

            ExerciseEntity("Dominadas", "Espalda", MovementPattern.PULL),
            ExerciseEntity("Dominadas supinas", "Espalda / Bíceps", MovementPattern.PULL),
            ExerciseEntity("Remo con barra", "Espalda", MovementPattern.PULL),
            ExerciseEntity("Remo unilateral con mancuerna", "Espalda", MovementPattern.PULL),
            ExerciseEntity("Jalón al pecho", "Espalda", MovementPattern.PULL),
            ExerciseEntity("Pájaros", "Hombro posterior", MovementPattern.PULL),
            ExerciseEntity("Face pull", "Hombro posterior / Espalda alta", MovementPattern.PULL),
            ExerciseEntity("Curl bíceps con barra", "Bíceps", MovementPattern.PULL),
            ExerciseEntity("Curl martillo", "Bíceps", MovementPattern.PULL),

            ExerciseEntity("Sentadilla", "Pierna", MovementPattern.LEGS),
            ExerciseEntity("Prensa de piernas", "Pierna", MovementPattern.LEGS),
            ExerciseEntity("Peso muerto rumano", "Femoral / Glúteo", MovementPattern.LEGS),
            ExerciseEntity("Zancadas", "Pierna / Glúteo", MovementPattern.LEGS),
            ExerciseEntity("Curl femoral", "Femoral", MovementPattern.LEGS),
            ExerciseEntity("Extensión de cuádriceps", "Cuádriceps", MovementPattern.LEGS),
            ExerciseEntity("Elevación de gemelos", "Gemelo", MovementPattern.LEGS),

            ExerciseEntity("Plancha", "Core", MovementPattern.CORE),
            ExerciseEntity("Elevaciones de piernas", "Core", MovementPattern.CORE),
            ExerciseEntity("Ab wheel", "Core", MovementPattern.CORE),
            ExerciseEntity("Russian twists", "Core", MovementPattern.CORE),
            ExerciseEntity("Pallof press", "Core", MovementPattern.CORE),
            ExerciseEntity("Dragon flag", "Core", MovementPattern.CORE)
        )
    }

    fun preloadExercises(
        exerciseDao: ExerciseDao,
        scope: CoroutineScope
    ) {
        scope.launch {
            defaultExercises().forEach { defaultExercise ->
                val existing = exerciseDao.getByName(defaultExercise.name)

                if (existing == null) {
                    exerciseDao.insert(defaultExercise)
                } else if (existing.movementPattern == null) {
                    exerciseDao.update(
                        existing.copy(
                            muscleGroup = defaultExercise.muscleGroup,
                            movementPattern = defaultExercise.movementPattern,
                            isPublic = true,
                            createdByUid = null
                        )
                    )
                }
            }
        }
    }

    private fun ExerciseEntity(
        name: String,
        muscleGroup: String,
        movementPattern: MovementPattern
    ): ExerciseEntity {
        return ExerciseEntity(
            name = name,
            muscleGroup = muscleGroup,
            movementPattern = movementPattern,
            isPublic = true,
            createdByUid = null
        )
    }
}