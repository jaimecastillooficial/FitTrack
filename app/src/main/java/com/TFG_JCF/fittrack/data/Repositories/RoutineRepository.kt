package com.TFG_JCF.fittrack.data.Repositories

import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayPlanDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineWeekDao
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayPlanEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineWeekEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoutineRepository @Inject constructor(
    private val routineWeekDao: RoutineWeekDao,
    private val routineDayPlanDao: RoutineDayPlanDao,
    private val routineDayExerciseDao: RoutineDayExerciseDao
) {

    suspend fun createRoutineWeek(userUid: String, name: String): Long {
        return withContext(Dispatchers.IO) {
            routineWeekDao.insert(
                RoutineWeekEntity(
                    userUid = userUid,
                    name = name
                )
            )
        }
    }

    suspend fun updateRoutineWeek(entity: RoutineWeekEntity) {
        withContext(Dispatchers.IO) {
            routineWeekDao.update(entity)
        }
    }

    suspend fun deleteRoutineWeek(entity: RoutineWeekEntity) {
        withContext(Dispatchers.IO) {
            routineWeekDao.delete(entity)
        }
    }

    suspend fun getAllWeeks(userUid: String): List<RoutineWeekEntity> {
        return withContext(Dispatchers.IO) {
            routineWeekDao.getAllByUser(userUid)
        }
    }

    suspend fun getActiveWeek(userUid: String): RoutineWeekEntity? {
        return withContext(Dispatchers.IO) {
            routineWeekDao.getActiveByUser(userUid)
        }
    }

    suspend fun setActiveWeek(userUid: String, weekId: Long) {
        withContext(Dispatchers.IO) {
            routineWeekDao.setActive(userUid, weekId)
        }
    }

    suspend fun createRoutineDay(
        routineWeekId: Long,
        dayOfWeek: Int,
        dayName: String
    ): Long {
        return withContext(Dispatchers.IO) {
            routineDayPlanDao.insert(
                RoutineDayPlanEntity(
                    routineWeekId = routineWeekId,
                    dayOfWeek = dayOfWeek,
                    dayName = dayName
                )
            )
        }
    }

    suspend fun updateRoutineDay(entity: RoutineDayPlanEntity) {
        withContext(Dispatchers.IO) {
            routineDayPlanDao.update(entity)
        }
    }

    suspend fun deleteRoutineDay(entity: RoutineDayPlanEntity) {
        withContext(Dispatchers.IO) {
            routineDayPlanDao.delete(entity)
        }
    }

    suspend fun getDaysForWeek(routineWeekId: Long): List<RoutineDayPlanEntity> {
        return withContext(Dispatchers.IO) {
            routineDayPlanDao.getByRoutineWeek(routineWeekId)
        }
    }

    suspend fun getRoutineWeekById(id: Long): RoutineWeekEntity? {
        return withContext(Dispatchers.IO) {
            routineWeekDao.getById(id)
        }
    }

    suspend fun getDayByWeekAndNumber(
        routineWeekId: Long,
        dayOfWeek: Int
    ): RoutineDayPlanEntity? {
        return withContext(Dispatchers.IO) {
            routineDayPlanDao.getByRoutineWeekAndDay(routineWeekId, dayOfWeek)
        }
    }

    suspend fun addExerciseToDay(
        dayPlanId: Long,
        exerciseId: Long,
        orderIndex: Int
    ): Long {
        return withContext(Dispatchers.IO) {
            routineDayExerciseDao.insert(
                RoutineDayExerciseEntity(
                    dayPlanId = dayPlanId,
                    exerciseId = exerciseId,
                    orderIndex = orderIndex
                )
            )
        }
    }

    suspend fun updateExerciseInDay(entity: RoutineDayExerciseEntity) {
        withContext(Dispatchers.IO) {
            routineDayExerciseDao.update(entity)
        }
    }

    suspend fun removeExerciseFromDay(entity: RoutineDayExerciseEntity) {
        withContext(Dispatchers.IO) {
            routineDayExerciseDao.delete(entity)
        }
    }

    suspend fun getExercisesForDay(dayPlanId: Long): List<RoutineDayExerciseEntity> {
        return withContext(Dispatchers.IO) {
            routineDayExerciseDao.getByDayPlan(dayPlanId)
        }
    }

    suspend fun clearDayExercises(dayPlanId: Long) {
        withContext(Dispatchers.IO) {
            routineDayExerciseDao.deleteAllForDayPlan(dayPlanId)
        }
    }
}