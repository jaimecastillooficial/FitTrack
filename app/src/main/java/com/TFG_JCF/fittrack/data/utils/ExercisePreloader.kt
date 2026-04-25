package com.TFG_JCF.fittrack.data.utils

import com.TFG_JCF.fittrack.data.database.dao.Workout.ExerciseDao
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object ExercisePreloader {

    fun preloadExercises(
        exerciseDao: ExerciseDao,
        scope: CoroutineScope
    ) {
        scope.launch {
            val alreadyExists = exerciseDao.getByName("Press banca") != null

            if (alreadyExists) return@launch

            val exercises = listOf(
                ExerciseEntity(name = "Press banca", muscleGroup = "Pecho", isPublic = true),
                ExerciseEntity(name = "Press inclinado con mancuernas", muscleGroup = "Pecho", isPublic = true),
                ExerciseEntity(name = "Fondos en paralelas", muscleGroup = "Pecho / Tríceps", isPublic = true),
                ExerciseEntity(name = "Aperturas con mancuernas", muscleGroup = "Pecho", isPublic = true),

                ExerciseEntity(name = "Dominadas", muscleGroup = "Espalda", isPublic = true),
                ExerciseEntity(name = "Dominadas supinas", muscleGroup = "Espalda / Bíceps", isPublic = true),
                ExerciseEntity(name = "Remo con barra", muscleGroup = "Espalda", isPublic = true),
                ExerciseEntity(name = "Remo unilateral con mancuerna", muscleGroup = "Espalda", isPublic = true),
                ExerciseEntity(name = "Jalón al pecho", muscleGroup = "Espalda", isPublic = true),

                ExerciseEntity(name = "Press militar", muscleGroup = "Hombro", isPublic = true),
                ExerciseEntity(name = "Elevaciones laterales", muscleGroup = "Hombro", isPublic = true),
                ExerciseEntity(name = "Pájaros", muscleGroup = "Hombro posterior", isPublic = true),
                ExerciseEntity(name = "Face pull", muscleGroup = "Hombro posterior / Espalda alta", isPublic = true),

                ExerciseEntity(name = "Curl bíceps con barra", muscleGroup = "Bíceps", isPublic = true),
                ExerciseEntity(name = "Curl martillo", muscleGroup = "Bíceps", isPublic = true),
                ExerciseEntity(name = "Extensión de tríceps en polea", muscleGroup = "Tríceps", isPublic = true),
                ExerciseEntity(name = "Extensión unilateral de tríceps", muscleGroup = "Tríceps", isPublic = true),

                ExerciseEntity(name = "Sentadilla", muscleGroup = "Pierna", isPublic = true),
                ExerciseEntity(name = "Prensa de piernas", muscleGroup = "Pierna", isPublic = true),
                ExerciseEntity(name = "Peso muerto rumano", muscleGroup = "Femoral / Glúteo", isPublic = true),
                ExerciseEntity(name = "Zancadas", muscleGroup = "Pierna / Glúteo", isPublic = true),
                ExerciseEntity(name = "Curl femoral", muscleGroup = "Femoral", isPublic = true),
                ExerciseEntity(name = "Extensión de cuádriceps", muscleGroup = "Cuádriceps", isPublic = true),
                ExerciseEntity(name = "Elevación de gemelos", muscleGroup = "Gemelo", isPublic = true),

                ExerciseEntity(name = "Plancha", muscleGroup = "Core", isPublic = true),
                ExerciseEntity(name = "Elevaciones de piernas", muscleGroup = "Core", isPublic = true),
                ExerciseEntity(name = "Ab wheel", muscleGroup = "Core", isPublic = true),
                ExerciseEntity(name = "Russian twists", muscleGroup = "Core", isPublic = true),
                ExerciseEntity(name = "Pallof press", muscleGroup = "Core", isPublic = true),
                ExerciseEntity(name = "Dragon flag", muscleGroup = "Core", isPublic = true)
            )

            exercises.forEach { exercise ->
                exerciseDao.insert(exercise)
            }
        }
    }
}