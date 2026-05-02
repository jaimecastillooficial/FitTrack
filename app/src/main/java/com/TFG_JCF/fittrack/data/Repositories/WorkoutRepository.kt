package com.TFG_JCF.fittrack.data.Repositories

import com.TFG_JCF.fittrack.data.database.Relations.Workout.WorkoutFull
import com.TFG_JCF.fittrack.data.database.dao.Workout.*
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineExerciseSetPlanEntity
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
    private val routineDayExerciseDao: RoutineDayExerciseDao,
    private val routineExerciseSetPlanDao: RoutineExerciseSetPlanDao
) {

    // GET WORKOUT COMPLETO

    suspend fun getWorkoutFullByDate(userUid: String, date: String): WorkoutFull? {
        return withContext(Dispatchers.IO) {
            workoutDao.getWorkoutFullByDate(userUid, date)
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
    suspend fun hasWorkoutForDate(userUid: String, date: String): Boolean {
        return withContext(Dispatchers.IO) {
            workoutDao.getByDate(userUid, date) != null
        }
    }

    suspend fun saveWorkoutFromDayPlanSets(
        userUid: String,
        date: String,
        dayPlanId: Long
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (workoutDao.getByDate(userUid, date) != null) {
                    return@withContext Result.failure(
                        Exception("Ya hay un entrenamiento registrado hoy")
                    )
                }

                val dayPlan = routineDayPlanDao.getById(dayPlanId)
                    ?: return@withContext Result.failure(
                        Exception("No se ha encontrado el plan de hoy")
                    )

                val routineExercises = routineDayExerciseDao.getRoutineDayExercisesByDayPlan(dayPlanId)

                if (routineExercises.isEmpty()) {
                    return@withContext Result.failure(
                        Exception("Este día no tiene ejercicios")
                    )
                }

                val plansByExercise = mutableMapOf<
                        RoutineDayExerciseEntity, List<RoutineExerciseSetPlanEntity>
                        >()

                routineExercises.forEach { relation ->
                    plansByExercise[relation] =
                        routineExerciseSetPlanDao.getSetsByRoutineDayExercise(relation.id)
                }

                val exerciseWithoutSets = plansByExercise.entries.firstOrNull {
                    it.value.isEmpty()
                }

                if (exerciseWithoutSets != null) {
                    return@withContext Result.failure(
                        Exception("Todos los ejercicios deben tener al menos una serie")
                    )
                }

                val workoutId = workoutDao.insert(
                    WorkoutEntity(
                        userUid = userUid,
                        date = date,
                        dayName = dayPlan.dayName
                    )
                )

                plansByExercise.forEach { (routineExercise, setPlans) ->
                    val workoutExerciseId = workoutExerciseDao.insert(
                        WorkoutExerciseEntity(
                            workoutId = workoutId,
                            exerciseId = routineExercise.exerciseId,
                            orderIndex = routineExercise.orderIndex
                        )
                    )

                    setPlans.forEachIndexed { index, plan ->
                        workoutSetDao.insert(
                            WorkoutSetEntity(
                                workoutExerciseId = workoutExerciseId,
                                setNumber = index + 1,
                                reps = plan.plannedReps,
                                weightKg = plan.plannedWeightKg,
                                rir = plan.plannedRir,
                                isWarmup = false
                            )
                        )
                    }
                }

                Result.success(Unit)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}



data class ExerciseWithSets(
    val exerciseId: Long,
    val orderIndex: Int,
    val sets: List<WorkoutSetEntity>
)