package com.TFG_JCF.fittrack.ui.MainApp.Home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.DietRepository
import com.TFG_JCF.fittrack.data.Repositories.UserRepository
import com.TFG_JCF.fittrack.data.utils.NutritionCalculator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dietRepository: DietRepository,
    private val userRepository: UserRepository
) : ViewModel() {

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

    private val _progressCalories = MutableStateFlow(0)
    val progressCalories: StateFlow<Int> = _progressCalories

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadHomeData() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val today = LocalDate.now().toString()

            val userProfile = userRepository.getUserProfile(uid)
            val goal = userProfile?.dailyCaloriesGoal ?: 0

            val meals = dietRepository.getMealsFullByDate(uid, today)

            val summary = NutritionCalculator.calculateDailySummary(meals)
            val macroTargets = NutritionCalculator.calculateMacroTargets(goal)

            _caloriesGoal.value = goal
            _caloriesConsumed.value = summary.caloriesConsumed
            _caloriesRemaining.value = (goal - summary.caloriesConsumed).coerceAtLeast(0)

            _proteinConsumed.value = summary.proteinConsumed
            _carbsConsumed.value = summary.carbsConsumed
            _fatsConsumed.value = summary.fatsConsumed

            _proteinGoal.value = macroTargets.proteinGoal
            _carbsGoal.value = macroTargets.carbsGoal
            _fatsGoal.value = macroTargets.fatsGoal

            _progressCalories.value = if (goal > 0) {
                ((summary.caloriesConsumed.toFloat() / goal.toFloat()) * 100).toInt().coerceIn(0, 100)
            } else {
                0
            }
        }
    }
}