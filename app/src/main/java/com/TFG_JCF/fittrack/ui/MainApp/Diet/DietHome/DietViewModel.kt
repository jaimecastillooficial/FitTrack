package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.DietRepository
import com.TFG_JCF.fittrack.data.UserRepository
import com.TFG_JCF.fittrack.data.database.Relations.Diet.MealWithItemsAndFoods
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType
import com.TFG_JCF.fittrack.data.model.MealListItem
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class DietViewModel @Inject constructor(
    private val dietRepository: DietRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<MealListItem>>(emptyList())
    val items: StateFlow<List<MealListItem>> = _items

    private val _caloriesGoal = MutableStateFlow(0)
    val caloriesGoal: StateFlow<Int> = _caloriesGoal

    private val _caloriesConsumed = MutableStateFlow(0)
    val caloriesConsumed: StateFlow<Int> = _caloriesConsumed

    private val _caloriesRemaining = MutableStateFlow(0)
    val caloriesRemaining: StateFlow<Int> = _caloriesRemaining

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDietForToday() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val today = LocalDate.now().toString() // YYYY-MM-DD

            val userProfile = userRepository.getUserProfile(uid)
            val goal = userProfile?.dailyCaloriesGoal ?: 0

            val mealsFromDb = dietRepository.getMealsFullByDate(uid, today)

            val uiList = buildMealList(mealsFromDb)
            val consumed = calculateConsumedCalories(mealsFromDb)
            val remaining = goal - consumed

            _items.value = uiList
            _caloriesGoal.value = goal
            _caloriesConsumed.value = consumed
            _caloriesRemaining.value = remaining
        }
    }

    private fun buildMealList(meals: List<MealWithItemsAndFoods>): List<MealListItem> {
        val finalList = mutableListOf<MealListItem>()

        val orderedMealTypes = listOf(
            MealType.DESAYUNO,
            MealType.COMIDA,
            MealType.CENA,
            MealType.SNACK
        )

        for (mealType in orderedMealTypes) {
            finalList.add(MealListItem.Header(mealType))

            val meal = meals.find { it.meal.type == mealType }

            meal?.items?.forEach { mealItemWithFood ->
                val food = mealItemWithFood.food
                val grams = mealItemWithFood.item.grams
                val calories = ((food.kcalPer100g * grams) / 100f).roundToInt()

                finalList.add(
                    MealListItem.FoodItem(
                        name = food.name,
                        calories = calories,
                        grams = grams.roundToInt()
                    )
                )
            }
        }

        return finalList
    }

    private fun calculateConsumedCalories(meals: List<MealWithItemsAndFoods>): Int {
        return meals.sumOf { meal ->
            meal.items.sumOf { itemWithFood ->
                ((itemWithFood.food.kcalPer100g * itemWithFood.item.grams) / 100f).roundToInt()
            }
        }
    }
}