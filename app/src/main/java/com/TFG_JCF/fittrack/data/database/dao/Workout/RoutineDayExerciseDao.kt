package com.TFG_JCF.fittrack.data.database.dao.Workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayExerciseEntity

@Dao
interface RoutineDayExerciseDao {

    @Insert
    suspend fun insert(entity: RoutineDayExerciseEntity): Long

    @Update
    suspend fun update(entity: RoutineDayExerciseEntity)

    @Delete
    suspend fun delete(entity: RoutineDayExerciseEntity)

    @Query("""
        SELECT * FROM routine_day_exercises
        WHERE dayPlanId = :dayPlanId
        ORDER BY orderIndex ASC
    """)
    suspend fun getByDayPlan(dayPlanId: Long): List<RoutineDayExerciseEntity>

    @Query("DELETE FROM routine_day_exercises WHERE dayPlanId = :dayPlanId")
    suspend fun deleteAllForDayPlan(dayPlanId: Long)
}