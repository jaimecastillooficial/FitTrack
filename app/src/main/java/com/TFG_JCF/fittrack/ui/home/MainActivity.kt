package com.TFG_JCF.fittrack.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.dao.Diet.FoodDao
import com.TFG_JCF.fittrack.data.database.dao.Diet.MealDao
import com.TFG_JCF.fittrack.data.database.dao.Diet.MealItemDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealItemEntity
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var foodDao: FoodDao
    @Inject lateinit var mealDao: MealDao
    @Inject lateinit var mealItemDao: MealItemDao
    @Inject lateinit var userProfileDao: UserProfileDao

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUI()
    }

    private fun initUI() {
        TestDatabase()
    }

    private fun TestDatabase() {

        val userUid = "TEST_UID"
        val today = "2026-02-13" // cámbialo o genera la fecha automáticamente

        lifecycleScope.launch {

            // 1) Asegurar alimento "Arroz" (para no meter 20 veces el mismo, lo buscamos por nombre)
            // Si no tienes query por nombre, lo hacemos simple: insertamos y ya (da igual en pruebas).
            val arroz = FoodEntity(
                name = "Arroz",
                kcalPer100g = 130,
                proteinPer100g = 2.5f,
                carbsPer100g = 28f,
                fatPer100g = 0.3f,
                isPublic = true,
                createdByUid = null
            )
            foodDao.insert(arroz)

            val foods = foodDao.getAll()
            val arrozId = foods.last().id  // para prueba rápida (mejorable luego)
            Log.d("ROOM_TEST", "Foods total=${foods.size}, arrozId=$arrozId")

            // 2) Crear o recuperar el desayuno de hoy
            val existingBreakfast = mealDao.getMeal(userUid, today, "DESAYUNO")
            val breakfastId = if (existingBreakfast == null) {
                val newId = mealDao.insert(
                    MealEntity(
                        userUid = userUid,
                        date = today,
                        type = "DESAYUNO",
                        name = "Desayuno de prueba"
                    )
                )
                Log.d("ROOM_TEST", "Breakfast created id=$newId")
                newId
            } else {
                Log.d("ROOM_TEST", "Breakfast exists id=${existingBreakfast.id}")
                existingBreakfast.id
            }

            // 3) Añadir item al desayuno
            val itemId = mealItemDao.insert(
                MealItemEntity(
                    mealId = breakfastId,
                    foodId = arrozId,
                    grams = 80f
                )
            )
            Log.d("ROOM_TEST", "MealItem inserted id=$itemId")

            // 4) Leer meals del día
            val mealsToday = mealDao.getMealsByDate(userUid, today)
            Log.d("ROOM_TEST", "Meals for $today = ${mealsToday.size}")

            // 5) Para cada meal, listar items
            mealsToday.forEach { meal ->
                Log.d("ROOM_TEST", "Meal: id=${meal.id} type=${meal.type} name=${meal.name}")
                val items = mealItemDao.getItemsByMeal(meal.id)
                Log.d("ROOM_TEST", "  Items: ${items.size}")
                items.forEach { it ->
                    Log.d(
                        "ROOM_TEST",
                        "   - mealId=${it.mealId} foodId=${it.foodId} grams=${it.grams}"
                    )
                }
            }

        }

    }
}