package com.TFG_JCF.fittrack.data

import com.TFG_JCF.fittrack.data.database.Relations.Diet.MealWithItemsAndFoods
import com.TFG_JCF.fittrack.data.database.dao.Diet.FoodDao
import com.TFG_JCF.fittrack.data.database.dao.Diet.MealDao
import com.TFG_JCF.fittrack.data.database.dao.Diet.MealItemDao
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DietRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val mealDao: MealDao,
    private val mealItemDao: MealItemDao
){
    //Obtener todos los alimentos
    suspend fun getAllFood(): List<FoodEntity> = withContext(Dispatchers.IO){

        return@withContext foodDao.getAll()

    }
    // crear un alimento
    suspend fun insertFood(name: String, kcal: Int, protein: Float, carbs: Float, fat: Float, isPublic: Boolean, createdByUid: String?) {
        withContext(Dispatchers.IO) {

            val food = FoodEntity(
                name = name,
                kcalPer100g = kcal,
                proteinPer100g = protein,
                carbsPer100g = carbs,
                fatPer100g = fat,
                isPublic = isPublic,
                createdByUid = createdByUid
            )

            foodDao.insert(food)
        }
    }
    //Obtener Meals con sus items y alimentos mediante la fecha
    suspend fun getMealsFullByDate(userUid: String, date: String): List<MealWithItemsAndFoods> = withContext(Dispatchers.IO){3
        return@withContext mealDao.getMealsWithItemsAndFoodsByDate(userUid= userUid, date = date)
    }
    // Insertar un alimento en una MeaL(Comida)
    suspend fun addFoodToMeal(
        userUid: String,
        date: String,      // YYYY-MM-DD
        type: String,      // DESAYUNO / COMIDA / CENA / SNACK
        foodId: Long,
        grams: Float,
        mealName: String? = null
    ): Long = withContext(Dispatchers.IO) {

        require(grams > 0f) { "grams debe ser > 0" }

        // 1) Buscar meal si no existe se crea
        val existingMeal = mealDao.getMeal(userUid, date, type)
        val mealId =
            if (existingMeal == null) {
                mealDao.insert(
                        MealEntity(
                            userUid = userUid,
                            date = date,
                            type = type,
                            name = mealName
                        )
                )
        } else {
            existingMeal.id
        }

        // 2) Insertar el item
        mealItemDao.insert(
            MealItemEntity(
                mealId = mealId,
                foodId = foodId,
                grams = grams
            )
        )
    }

    suspend fun getOrCreateMeal(
        userUid: String,
        date: String,
        type: String,
        mealName: String? = null
    ): MealEntity = withContext(Dispatchers.IO) {

        val existing = mealDao.getMeal(userUid, date, type)

        if (existing != null) {
            existing
        } else {
            val newId = mealDao.insert(
                MealEntity(
                    userUid = userUid,
                    date = date,
                    type = type,
                    name = mealName
                )
            )

            // Devolvemos la entidad creada
            MealEntity(
                id = newId,
                userUid = userUid,
                date = date,
                type = type,
                name = mealName
            )
        }
    }

    suspend fun deleteMealItem(
        mealItemId: Long
    ) = withContext(Dispatchers.IO) {
        mealItemDao.deleteById(mealItemId)
    }

    suspend fun getMealFull(
        userUid: String,
        date: String,
        type: String
    ) = withContext(Dispatchers.IO) {
        mealDao.getMealWithItemsAndFoods(userUid, date, type)
    }


    //Funcion que coja los alimentos de una meal y calcule el total de calorias, proteinas, grasas y carbohidratos

}