package com.TFG_JCF.fittrack.data.database.dao.Workout

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.TFG_JCF.fittrack.data.database.Relations.Workout.WorkoutFull
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

    @Transaction
    @Query("""
    SELECT * FROM workouts
    WHERE userUid = :userUid AND date = :date
""")
    suspend fun getWorkoutFullByDate(userUid: String, date: String): WorkoutFull?

    @Transaction
    @Query("""
    SELECT * FROM workouts
    WHERE userUid = :userUid AND date BETWEEN :fromDate AND :toDate
    ORDER BY date ASC
""")
    // El tipo de retorno es decir cuando le indicas que va a devolver es lo que hace que ROOM utilize la clase Relation
    suspend fun getWorkoutsFullByRange(userUid: String, fromDate: String, toDate: String): List<WorkoutFull>

}

