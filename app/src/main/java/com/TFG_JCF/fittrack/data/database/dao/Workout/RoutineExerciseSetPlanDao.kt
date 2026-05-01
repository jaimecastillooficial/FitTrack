package com.TFG_JCF.fittrack.data.database.dao.Workout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineExerciseSetPlanEntity

@Dao
interface RoutineExerciseSetPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RoutineExerciseSetPlanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<RoutineExerciseSetPlanEntity>)

    @Query("DELETE FROM routine_exercise_set_plans WHERE routineDayExerciseId = :routineDayExerciseId")
    suspend fun deleteByRoutineDayExercise(routineDayExerciseId: Long)

    @Query("""
        SELECT * FROM routine_exercise_set_plans
        WHERE routineDayExerciseId = :routineDayExerciseId
        ORDER BY setNumber ASC
    """)
    suspend fun getByRoutineDayExercise(routineDayExerciseId: Long): List<RoutineExerciseSetPlanEntity>

    @Transaction
    suspend fun replaceForRoutineDayExercise(
        routineDayExerciseId: Long,
        plans: List<RoutineExerciseSetPlanEntity>
    ) {
        deleteByRoutineDayExercise(routineDayExerciseId)

        if (plans.isNotEmpty()) {
            insertAll(plans)
        }
    }
}