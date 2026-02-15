package com.TFG_JCF.fittrack.data.database.dao.Workout


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutSetEntity

@Dao
interface WorkoutSetDao {

    @Insert
    suspend fun insert(set: WorkoutSetEntity): Long

    @Update
    suspend fun update(set: WorkoutSetEntity)

    @Delete
    suspend fun delete(set: WorkoutSetEntity)

    @Query("DELETE FROM workout_sets WHERE workoutExerciseId = :workoutExerciseId")
    suspend fun deleteAllForExercise(workoutExerciseId: Long)

    @Query("""
        SELECT * FROM workout_sets
        WHERE workoutExerciseId = :workoutExerciseId
        ORDER BY setNumber ASC
    """)
    suspend fun getByWorkoutExercise(workoutExerciseId: Long): List<WorkoutSetEntity>
}

