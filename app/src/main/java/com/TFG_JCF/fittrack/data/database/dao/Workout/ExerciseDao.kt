package com.TFG_JCF.fittrack.data.database.dao.Workout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insert(exercise: ExerciseEntity): Long

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ExerciseEntity?

    @Query("SELECT * FROM exercises WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): ExerciseEntity?

    // Predefinidos (isPublic = 1) + privados del usuario (createdByUid = userUid)
    @Query("""
        SELECT * FROM exercises
        WHERE isPublic = 1 OR createdByUid = :userUid
        ORDER BY name ASC
    """)
    suspend fun getVisibleForUser(userUid: String): List<ExerciseEntity>

    @Query("DELETE FROM exercises WHERE id = :id")
    suspend fun deleteById(id: Long)
}
