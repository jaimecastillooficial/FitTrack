package com.TFG_JCF.fittrack.data.database.dao.Workout

import androidx.room.*
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayPlanEntity

@Dao
interface RoutineDayPlanDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: RoutineDayPlanEntity): Long

    @Query("SELECT * FROM routine_day_plans WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): RoutineDayPlanEntity?

    @Update
    suspend fun update(entity: RoutineDayPlanEntity)

    @Delete
    suspend fun delete(entity: RoutineDayPlanEntity)

    @Query("""
        SELECT * FROM routine_day_plans
        WHERE routineWeekId = :routineWeekId
        ORDER BY dayOfWeek ASC
    """)
    suspend fun getByRoutineWeek(routineWeekId: Long): List<RoutineDayPlanEntity>

    // Metodo para obtener un plan de rutina de un dia en concreto
    @Query("""
        SELECT * FROM routine_day_plans
        WHERE routineWeekId = :routineWeekId AND dayOfWeek = :dayOfWeek
        LIMIT 1
    """)
    suspend fun getByRoutineWeekAndDay(
        routineWeekId: Long,
        dayOfWeek: Int
    ): RoutineDayPlanEntity?
}
