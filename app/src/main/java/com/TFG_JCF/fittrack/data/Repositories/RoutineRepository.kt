package com.TFG_JCF.fittrack.data.Repositories

import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayPlanDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineExerciseSetPlanDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineWeekDao
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayPlanEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineExerciseSetPlanEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineWeekEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoutineRepository @Inject constructor(
    private val routineWeekDao: RoutineWeekDao,
    private val routineDayPlanDao: RoutineDayPlanDao,
    private val routineDayExerciseDao: RoutineDayExerciseDao,
    private val routineExerciseSetPlanDao: RoutineExerciseSetPlanDao
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
    suspend fun getDayPlanById(id: Long): RoutineDayPlanEntity? {
        return withContext(Dispatchers.IO) {
            routineDayPlanDao.getById(id)
        }
    }
    //Devuelve las series de un ejercicio por ID_ejercicio del dia
    suspend fun getSetPlansForRoutineExercise(
        routineDayExerciseId: Long
    ): List<RoutineExerciseSetPlanEntity> {
        return withContext(Dispatchers.IO) {
            routineExerciseSetPlanDao.getSetsByRoutineDayExercise(routineDayExerciseId)
        }
    }

    //Obtiene las series de un ejercicio cuando solo tengo el bloque y el exerciseId.
    suspend fun getSetPlansForExerciseInBlock(
        dayPlanIds: List<Long>,
        exerciseId: Long
    ): List<RoutineExerciseSetPlanEntity> {
        return withContext(Dispatchers.IO) {
            val referenceDayPlanId = dayPlanIds.firstOrNull()
                ?: return@withContext emptyList()

            //Compruba que el ejercicio este en ese dia
            val relation = routineDayExerciseDao.getRoutineDayExerciseByDayPlanAndExercise(
                dayPlanId = referenceDayPlanId,
                exerciseId = exerciseId
            ) ?: return@withContext emptyList()

            routineExerciseSetPlanDao.getSetsByRoutineDayExercise(relation.id)
        }
    }
    // Guarda las series del ejercicio en todos los días donde aparece el bloque.
    suspend fun saveSetPlansForExerciseInBlock(
        dayPlanIds: List<Long>,
        exerciseId: Long,
        setPlans: List<RoutineExerciseSetPlanEntity>
    ) {
        withContext(Dispatchers.IO) {

            dayPlanIds.forEach { dayPlanId ->
                val relation = routineDayExerciseDao.getRoutineDayExerciseByDayPlanAndExercise(dayPlanId, exerciseId)
                    ?: return@forEach

                val plansForRelation = setPlans.mapIndexed { index, plan ->
                    plan.copy(
                        //id = 0 = Room genera un ID nuevo para esta fila
                        id = 0,
                        routineDayExerciseId = relation.id,
                        setNumber = index + 1
                    )
                }

                routineExerciseSetPlanDao.replaceForRoutineDayExercise(
                    routineDayExerciseId = relation.id,
                    plans = plansForRelation
                )
            }
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
            routineDayExerciseDao.getRoutineDayExercisesByDayPlan(dayPlanId)
        }
    }

    suspend fun clearDayExercises(dayPlanId: Long) {
        withContext(Dispatchers.IO) {
            routineDayExerciseDao.deleteAllForDayPlan(dayPlanId)
        }
    }
    //Metodo para saber si existe una rutina con el mismo nombre
    suspend fun existsRoutineWithName(userUid: String, name: String): Boolean {
        return withContext(Dispatchers.IO) {
            val routines = routineWeekDao.getAllByUser(userUid)

            routines.any {
                it.name.trim().equals(name.trim(), ignoreCase = true)
            }
        }
    }
    suspend fun copyExercisesToDay(
        fromDayPlanId: Long,
        toDayPlanId: Long
    ) {
        withContext(Dispatchers.IO) {
            //Obtiene los ejercicios del dia original
            val sourceExercises = routineDayExerciseDao.getRoutineDayExercisesByDayPlan(fromDayPlanId)
        //Los inserta
            sourceExercises.forEach { exercise ->
                val newRelationId = routineDayExerciseDao.insert(
                    RoutineDayExerciseEntity(
                        dayPlanId = toDayPlanId,
                        exerciseId = exercise.exerciseId,
                        orderIndex = exercise.orderIndex
                    )
                )

                val sourceSetPlans = routineExerciseSetPlanDao.getSetsByRoutineDayExercise(exercise.id)

                val copiedSetPlans = sourceSetPlans.map { plan ->
                    plan.copy(
                        id = 0,
                        routineDayExerciseId = newRelationId
                    )
                }

                routineExerciseSetPlanDao.replaceForRoutineDayExercise(
                    routineDayExerciseId = newRelationId,
                    plans = copiedSetPlans
                )
            }
        }
    }
    suspend fun addExerciseToBlock(
        dayPlanIds: List<Long>,
        exerciseId: Long
    ) {
        withContext(Dispatchers.IO) {
            if (dayPlanIds.isEmpty()) return@withContext

            val referenceExercises = routineDayExerciseDao.getRoutineDayExercisesByDayPlan(dayPlanIds.first())

            val alreadyExists = referenceExercises.any { it.exerciseId == exerciseId }
            if (alreadyExists) return@withContext

            val nextOrderIndex = (referenceExercises.maxOfOrNull { it.orderIndex } ?: -1) + 1

            dayPlanIds.forEach { dayPlanId ->
                routineDayExerciseDao.insert(
                    RoutineDayExerciseEntity(
                        dayPlanId = dayPlanId,
                        exerciseId = exerciseId,
                        orderIndex = nextOrderIndex
                    )
                )
            }
        }
    }

    suspend fun removeExerciseFromBlock(
        dayPlanIds: List<Long>,
        exerciseId: Long
    ) {
        withContext(Dispatchers.IO) {
            dayPlanIds.forEach { dayPlanId ->
                val relations = routineDayExerciseDao.getRoutineDayExercisesByDayPlan(dayPlanId)
                val relationToDelete = relations.firstOrNull { it.exerciseId == exerciseId }

                if (relationToDelete != null) {
                    routineDayExerciseDao.delete(relationToDelete)
                }
            }
        }
    }

}