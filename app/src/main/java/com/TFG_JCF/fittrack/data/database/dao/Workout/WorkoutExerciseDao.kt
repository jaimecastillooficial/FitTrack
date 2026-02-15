package com.TFG_JCF.fittrack.data.database.dao.Workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutExerciseEntity

@Dao
interface WorkoutExerciseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: WorkoutExerciseEntity): Long

    @Update
    suspend fun update(entity: WorkoutExerciseEntity)

    @Delete
    suspend fun delete(entity: WorkoutExerciseEntity)

    @Query("DELETE FROM workout_exercises WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT * FROM workout_exercises
        WHERE workoutId = :workoutId
        ORDER BY orderIndex ASC
    """)
    suspend fun getByWorkout(workoutId: Long): List<WorkoutExerciseEntity>

    @Query("""
        SELECT * FROM workout_exercises
        WHERE workoutId = :workoutId AND exerciseId = :exerciseId
        LIMIT 1
    """)
    suspend fun getByWorkoutAndExercise(workoutId: Long, exerciseId: Long): WorkoutExerciseEntity?
}

