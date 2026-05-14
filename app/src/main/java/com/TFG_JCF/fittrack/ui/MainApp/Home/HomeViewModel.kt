package com.TFG_JCF.fittrack.ui.MainApp.Home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.DietRepository
import com.TFG_JCF.fittrack.data.Repositories.ProgressRepository
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.Repositories.UserRepository
import com.TFG_JCF.fittrack.data.Repositories.WorkoutRepository
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.TFG_JCF.fittrack.data.utils.NutritionCalculator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dietRepository: DietRepository,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository,
    private val progressRepository: ProgressRepository,
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _caloriesGoal = MutableStateFlow(0)
    val caloriesGoal: StateFlow<Int> = _caloriesGoal

    private val _caloriesConsumed = MutableStateFlow(0)
    val caloriesConsumed: StateFlow<Int> = _caloriesConsumed

    private val _caloriesRemaining = MutableStateFlow("")
    val caloriesRemaining: StateFlow<String> = _caloriesRemaining

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

    private val _todayWorkoutName = MutableStateFlow("Sin entreno")
    val todayWorkoutName: StateFlow<String> = _todayWorkoutName

    private val _todayWorkoutExercises = MutableStateFlow("Hoy")
    val todayWorkoutExercises: StateFlow<String> = _todayWorkoutExercises

    private val _lastWorkoutName = MutableStateFlow("Sin datos")
    val lastWorkoutName: StateFlow<String> = _lastWorkoutName

    private val _lastWorkoutDate = MutableStateFlow("Registra un entreno")
    val lastWorkoutDate: StateFlow<String> = _lastWorkoutDate

    private val _weightEntries = MutableStateFlow<List<WeightEntryEntity>>(emptyList())
    val weightEntries: StateFlow<List<WeightEntryEntity>> = _weightEntries

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadHomeData() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val today = LocalDate.now().toString()

            loadDietData(uid, today)
            loadWorkoutData(uid, today)
            loadProgressData(uid)
        }
    }

    private suspend fun loadDietData(uid: String, today: String) {
        val userProfile = userRepository.getUserProfile(uid)
        val goal = userProfile?.dailyCaloriesGoal ?: 0

        val meals = dietRepository.getMealsFullByDate(uid, today)

        val summary = NutritionCalculator.calculateDailySummary(meals)
        val macroTargets = NutritionCalculator.calculateMacroTargets(goal)

        _caloriesGoal.value = goal
        _caloriesConsumed.value = summary.caloriesConsumed

        if (goal - summary.caloriesConsumed < 0) {
            _caloriesRemaining.value = "+${summary.caloriesConsumed - goal}"
        } else {
            _caloriesRemaining.value = "${goal - summary.caloriesConsumed}"
        }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadWorkoutData(uid: String, today: String) {

        // Rutina que toca hoy
        val activeWeek = routineRepository.getActiveWeek(uid)

        if (activeWeek == null) {
            _todayWorkoutName.value = "Sin rutina"
            _todayWorkoutExercises.value = "No hay rutina activa"
        } else {
            val todayNumber = getTodayAsRoutineDayNumber()

            val todayPlan = routineRepository.getDayByWeekAndNumber(
                routineWeekId = activeWeek.id,
                dayOfWeek = todayNumber
            )

            if (todayPlan == null) {
                _todayWorkoutName.value = "Descanso"
                _todayWorkoutExercises.value = "Hoy no tienes entreno"
            } else {
                val exercises = routineRepository.getExercisesForDay(todayPlan.id)

                _todayWorkoutName.value = todayPlan.dayName

                _todayWorkoutExercises.value = when (exercises.size) {
                    0 -> "Sin ejercicios"
                    1 -> "1 ejercicio"
                    else -> "${exercises.size} ejercicios"
                }
            }
        }

        // Último entreno registrado
        val history = workoutRepository.getWorkoutHistory(uid)
        val lastWorkout = history.firstOrNull()

        if (lastWorkout != null) {
            _lastWorkoutName.value = lastWorkout.workout.dayName
            _lastWorkoutDate.value = formatWorkoutDate(lastWorkout.workout.date)
        } else {
            _lastWorkoutName.value = "Sin datos"
            _lastWorkoutDate.value = "Registra un entreno"
        }
    }

    private suspend fun loadProgressData(uid: String) {
        _weightEntries.value = progressRepository.getWeightEntriesByUser(uid)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatWorkoutDate(date: String): String {
        val workoutDate = LocalDate.parse(date)
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(workoutDate, today)

        return when (days) {
            0L -> "Hoy"
            1L -> "Ayer"
            else -> date
        }
    }
    private fun getTodayAsRoutineDayNumber(): Int {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            Calendar.SUNDAY -> 7
            else -> 1
        }
    }
}