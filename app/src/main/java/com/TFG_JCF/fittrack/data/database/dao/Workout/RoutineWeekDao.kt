package com.TFG_JCF.fittrack.data.database.dao.Workout


import androidx.room.*
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineWeekEntity

@Dao
interface RoutineWeekDao {

    @Insert
    suspend fun insert(entity: RoutineWeekEntity): Long

    @Update
    suspend fun update(entity: RoutineWeekEntity)

    @Delete
    suspend fun delete(entity: RoutineWeekEntity)

    @Query("SELECT * FROM routine_weeks WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): RoutineWeekEntity?

    @Query("""
        SELECT * FROM routine_weeks
        WHERE userUid = :userUid
        ORDER BY id DESC
    """)
    suspend fun getAllByUser(userUid: String): List<RoutineWeekEntity>

    @Query("""
        SELECT * FROM routine_weeks
        WHERE userUid = :userUid AND isActive = 1
        LIMIT 1
    """)
    suspend fun getActiveByUser(userUid: String): RoutineWeekEntity?

    @Query("""
        UPDATE routine_weeks
        SET isActive = 0
        WHERE userUid = :userUid
    """)
    suspend fun deactivateAll(userUid: String)

    @Transaction
    suspend fun setActive(userUid: String, routineWeekId: Long) {
        deactivateAll(userUid)
        val routine = getById(routineWeekId) ?: return
        update(routine.copy(isActive = true))
    }
}
