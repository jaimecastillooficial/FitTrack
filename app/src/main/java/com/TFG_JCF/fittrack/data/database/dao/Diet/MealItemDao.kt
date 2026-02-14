package com.TFG_JCF.fittrack.data.database.dao.Diet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealItemEntity

@Dao
interface MealItemDao {

    @Insert
    suspend fun insert(item: MealItemEntity): Long

    @Query("SELECT * FROM meal_items WHERE mealId = :mealId")
    suspend fun getItemsByMeal(mealId: Long): List<MealItemEntity>

    @Query("DELETE FROM meal_items WHERE id = :itemId")
    suspend fun deleteById(itemId: Long)

    @Query("DELETE FROM meal_items WHERE mealId = :mealId")
    suspend fun deleteAllForMeal(mealId: Long)
}
