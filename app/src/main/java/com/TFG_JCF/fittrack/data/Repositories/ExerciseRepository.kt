package com.TFG_JCF.fittrack.data.Repositories

import com.TFG_JCF.fittrack.data.database.dao.Workout.ExerciseDao
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val exerciseDao: ExerciseDao
) {

    suspend fun getExerciseById(id: Long): ExerciseEntity? {
        return withContext(Dispatchers.IO) {
            exerciseDao.getById(id)
        }
    }

    suspend fun getVisibleExercisesForUser(userUid: String): List<ExerciseEntity> {
        return withContext(Dispatchers.IO) {
            exerciseDao.getVisibleForUser(userUid)
        }
    }
    suspend fun prepareDefaultExercises(defaultExercises: List<ExerciseEntity>) {
        withContext(Dispatchers.IO) {
            val alreadyExists = exerciseDao.getByName("Press banca") != null

            if (alreadyExists) return@withContext

            defaultExercises.forEach { exercise ->
                exerciseDao.insert(exercise)
            }
        }
    }
}