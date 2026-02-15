package com.TFG_JCF.fittrack.data.database.dao.Workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutEntity

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(workout: WorkoutEntity): Long

    @Update
    suspend fun update(workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    @Query("DELETE FROM workouts WHERE id = :workoutId")
    suspend fun deleteById(workoutId: Long)

    @Query("""
        SELECT * FROM workouts
        WHERE userUid = :userUid AND date = :date
        LIMIT 1
    """)
    suspend fun getByDate(userUid: String, date: String): WorkoutEntity?

    @Query("""
        SELECT * FROM workouts
        WHERE userUid = :userUid
        ORDER BY date DESC
    """)
    suspend fun getAllByUser(userUid: String): List<WorkoutEntity>
}

