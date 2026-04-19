package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.DietRepository
import com.TFG_JCF.fittrack.data.Repositories.UserRepository
import com.TFG_JCF.fittrack.data.database.Relations.Diet.MealWithItemsAndFoods
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType
import com.TFG_JCF.fittrack.data.model.Diet.MealListItem
import com.TFG_JCF.fittrack.data.utils.NutritionCalculator
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

    private val _proteinConsumed = MutableStateFlow(0f)
    val proteinConsumed: StateFlow<Float> = _proteinConsumed

    private val _carbsConsumed = MutableStateFlow(0f)
    val carbsConsumed: StateFlow<Float> = _carbsConsumed

    private val _fatsConsumed = MutableStateFlow(0f)
    val fatsConsumed: StateFlow<Float> = _fatsConsumed

    private val _proteinGoal = MutableStateFlow(0)
    val proteinGoal: StateFlow<Int> = _proteinGoal

    private val _carbsGoal = MutableStateFlow(0)
    val carbsGoal: StateFlow<Int> = _carbsGoal

    private val _fatsGoal = MutableStateFlow(0)
    val fatsGoal: StateFlow<Int> = _fatsGoal

    @RequiresApi(Build.VERSION_CODES.O)
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedDate: StateFlow<LocalDate> = _selectedDate


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDietForSelectedDate() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val selectedDateString = _selectedDate.value.toString()

            val userProfile = userRepository.getUserProfile(uid)
            val caloriesGoal = userProfile?.dailyCaloriesGoal ?: 0

            val meals = dietRepository.getMealsFullByDate(uid, selectedDateString)

            val mealList = buildMealList(meals)
            val summary = NutritionCalculator.calculateDailySummary(meals)
            val macroTargets = NutritionCalculator.calculateMacroTargets(caloriesGoal)

            _items.value = mealList

            _caloriesGoal.value = caloriesGoal
            _caloriesConsumed.value = summary.caloriesConsumed
            _caloriesRemaining.value = caloriesGoal - summary.caloriesConsumed

            _proteinConsumed.value = summary.proteinConsumed
            _carbsConsumed.value = summary.carbsConsumed
            _fatsConsumed.value = summary.fatsConsumed

            _proteinGoal.value = macroTargets.proteinGoal
            _carbsGoal.value = macroTargets.carbsGoal
            _fatsGoal.value = macroTargets.fatsGoal
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
                val item = mealItemWithFood.item
                val grams = mealItemWithFood.item.grams
                val calories = ((food.kcalPer100g * grams) / 100f).roundToInt()

                finalList.add(
                    MealListItem.FoodItem(
                        mealItemId = item.id,
                        name = food.name,
                        calories = calories,
                        grams = grams.roundToInt()
                    )
                )
            }
        }

        return finalList
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteMealItem(mealItemId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            dietRepository.deleteMealItem(mealItemId)
            loadDietForSelectedDate()
            onSuccess()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMealItemGrams(mealItemId: Long, grams: Float, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (grams <= 0f) return@launch
            dietRepository.updateMealItemGrams(mealItemId, grams)
            loadDietForSelectedDate()
            onSuccess()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSelectedDate(newDate: LocalDate) {
        _selectedDate.value = newDate
        loadDietForSelectedDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun goToPreviousDay() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
        loadDietForSelectedDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun goToNextDay() {
        val tomorrow = _selectedDate.value.plusDays(1)
        val today = LocalDate.now()

        if (tomorrow.isAfter(today)) return

        _selectedDate.value = tomorrow
        loadDietForSelectedDate()
    }

}