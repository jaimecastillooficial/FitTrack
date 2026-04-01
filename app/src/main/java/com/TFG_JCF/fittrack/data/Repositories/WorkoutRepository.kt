package com.TFG_JCF.fittrack.data.Repositories

import com.TFG_JCF.fittrack.data.database.Relations.Workout.WorkoutFull
import com.TFG_JCF.fittrack.data.database.dao.Workout.*
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutSetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val workoutSetDao: WorkoutSetDao,
    private val routineDayPlanDao: RoutineDayPlanDao,
    private val routineDayExerciseDao: RoutineDayExerciseDao
) {


    // GET WORKOUT COMPLETO


    suspend fun getWorkoutFullByDate(userUid: String, date: String): WorkoutFull? {
        return withContext(Dispatchers.IO) {
            workoutDao.getWorkoutFullByDate(userUid, date)
        }
    }


    // CREAR WORKOUT DESDE PLAN


    suspend fun createWorkoutFromPlan(
        userUid: String,
        date: String,
        routineWeekId: Long,
        dayOfWeek: Int
    ): Long {

        return withContext(Dispatchers.IO) {

            val dayPlan =
                routineDayPlanDao.getByRoutineWeekAndDay(routineWeekId, dayOfWeek)
                    ?: return@withContext -1L

            val workoutId = workoutDao.insert(
                WorkoutEntity(
                    userUid = userUid,
                    date = date,
                    dayName = dayPlan.dayName
                )
            )

            val exercises = routineDayExerciseDao.getByDayPlan(dayPlan.id)

            for (e in exercises) {
                workoutExerciseDao.insert(
                    WorkoutExerciseEntity(
                        workoutId = workoutId,
                        exerciseId = e.exerciseId,
                        orderIndex = e.orderIndex
                    )
                )
            }

            workoutId
        }
    }


    // GUARDAR ENTRENAMIENTO COMPLETO (BOTÓN FINAL)


    suspend fun saveFullWorkout(
        userUid: String,
        date: String,
        dayName: String,
        exercises: List<ExerciseWithSets>
    ) {

        withContext(Dispatchers.IO) {

            // Crear workout si no existe
            val existing = workoutDao.getByDate(userUid, date)

            val workoutId = if (existing != null) {
                existing.id
            } else {
                workoutDao.insert(
                    WorkoutEntity(
                        userUid = userUid,
                        date = date,
                        dayName = dayName
                    )
                )
            }

            for (exercise in exercises) {

                val existingExercise =
                    workoutExerciseDao.getByWorkoutAndExercise(
                        workoutId,
                        exercise.exerciseId
                    )

                val workoutExerciseId = if (existingExercise != null) {
                    existingExercise.id
                } else {
                    workoutExerciseDao.insert(
                        WorkoutExerciseEntity(
                            workoutId = workoutId,
                            exerciseId = exercise.exerciseId,
                            orderIndex = exercise.orderIndex
                        )
                    )
                }

                // Reemplazar sets
                workoutSetDao.deleteAllForExercise(workoutExerciseId)

                for (set in exercise.sets) {
                    workoutSetDao.insert(
                        set.copy(workoutExerciseId = workoutExerciseId)
                    )
                }
            }
        }
    }


    // GUARDAR SOLO UN EJERCICIO

    suspend fun saveSetsForExercise(
        workoutId: Long,
        exerciseId: Long,
        orderIndex: Int,
        sets: List<WorkoutSetEntity>
    ) {

        withContext(Dispatchers.IO) {

            val existingExercise =
                workoutExerciseDao.getByWorkoutAndExercise(workoutId, exerciseId)

            val workoutExerciseId = if (existingExercise != null) {
                existingExercise.id
            } else {
                workoutExerciseDao.insert(
                    WorkoutExerciseEntity(
                        workoutId = workoutId,
                        exerciseId = exerciseId,
                        orderIndex = orderIndex
                    )
                )
            }

            workoutSetDao.deleteAllForExercise(workoutExerciseId)

            for (set in sets) {
                workoutSetDao.insert(
                    set.copy(workoutExerciseId = workoutExerciseId)
                )
            }
        }
    }


    // BORRAR WORKOUT


    suspend fun deleteWorkoutByDate(userUid: String, date: String) {
        withContext(Dispatchers.IO) {
            val workout = workoutDao.getByDate(userUid, date)
            if (workout != null) {
                workoutDao.deleteById(workout.id)
            }
        }
    }
}

data class ExerciseWithSets(
    val exerciseId: Long,
    val orderIndex: Int,
    val sets: List<WorkoutSetEntity>
)