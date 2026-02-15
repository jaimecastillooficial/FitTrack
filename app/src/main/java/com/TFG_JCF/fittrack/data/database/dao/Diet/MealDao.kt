package com.TFG_JCF.fittrack.data.database.dao.Diet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.TFG_JCF.fittrack.data.database.Relations.MealWithItemsAndFoods
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealEntity

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(meal: MealEntity): Long

    @Delete
    suspend fun delete(meal: MealEntity)

    @Query("""
        SELECT * FROM meals
        WHERE id = :id
        
    """)
    suspend fun getMealById(id: Long): MealEntity?
    @Query("""
        SELECT * FROM meals
        WHERE userUid = :userUid AND date = :date
        ORDER BY 
            CASE type
                WHEN 'DESAYUNO' THEN 1
                WHEN 'COMIDA' THEN 2
                WHEN 'CENA' THEN 3
                WHEN 'SNACK' THEN 4
                ELSE 5
            END
    """)
    suspend fun getMealsByDate(userUid: String, date: String): List<MealEntity>

    @Query("""
        SELECT * FROM meals
        WHERE userUid = :userUid AND date = :date AND type = :type
        
    """)
    suspend fun getMeal(userUid: String, date: String, type: String): MealEntity?

    @Transaction
    @Query("""
    SELECT * FROM meals
    WHERE userUid = :userUid AND date = :date
    ORDER BY 
        CASE type
            WHEN 'DESAYUNO' THEN 1
            WHEN 'COMIDA' THEN 2
            WHEN 'CENA' THEN 3
            WHEN 'SNACK' THEN 4
            ELSE 5
        END
""")
    suspend fun getMealsWithItemsAndFoodsByDate(
        userUid: String,
        date: String
    ): List<MealWithItemsAndFoods>

    @Transaction
    @Query("""
    SELECT * FROM meals
    WHERE userUid = :userUid AND date = :date AND type = :type
    LIMIT 1
""")
    suspend fun getMealWithItemsAndFoods(
        userUid: String,
        date: String,
        type: String
    ): MealWithItemsAndFoods?


    @Query("DELETE FROM meals WHERE id = :mealId")
    suspend fun deleteById(mealId: Long)


}
