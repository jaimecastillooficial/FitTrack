package com.TFG_JCF.fittrack.data.database.dao.Diet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity

@Dao
interface FoodDao {

    @Insert
    suspend fun insert(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<FoodEntity>)

    @Query("SELECT * FROM foods")
    suspend fun getAll(): List<FoodEntity>

    @Query("SELECT * FROM foods WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): FoodEntity?

    @Query("SELECT * FROM foods WHERE id = :id")
    suspend fun getFoodById(id: Long): FoodEntity?

}
