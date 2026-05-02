package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.ExerciseRepository
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.Repositories.WorkoutRepository
import com.TFG_JCF.fittrack.data.model.Workout.RoutineExerciseItemUi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RoutineExerciseViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _blockTitle = MutableStateFlow("")
    val blockTitle: StateFlow<String> = _blockTitle.asStateFlow()

    private val _items = MutableStateFlow<List<RoutineExerciseItemUi>>(emptyList())
    val items: StateFlow<List<RoutineExerciseItemUi>> = _items.asStateFlow()

    private val _canRegisterWorkout = MutableStateFlow(true)
    val canRegisterWorkout: StateFlow<Boolean> = _canRegisterWorkout.asStateFlow()

    private var currentDayPlanIds: List<Long> = emptyList()

    fun loadBlockExercises(
        blockTitle: String,
        dayPlanIds: List<Long>
    ) {
        viewModelScope.launch {
            _blockTitle.value = blockTitle
            currentDayPlanIds = dayPlanIds

            reloadExercises()
            checkTodayWorkoutState()
        }
    }

    fun reloadCurrentBlock() {
        viewModelScope.launch {
            reloadExercises()
            checkTodayWorkoutState()
        }
    }

    private suspend fun reloadExercises() {
        if (currentDayPlanIds.isEmpty()) {
            _items.value = emptyList()
            return
        }

        val referenceDayPlanId = currentDayPlanIds.first()
        val relations = routineRepository.getExercisesForDay(referenceDayPlanId)

        val uiItems = relations.mapNotNull { relation ->
            val exercise = exerciseRepository.getExerciseById(relation.exerciseId)

            exercise?.let {
                RoutineExerciseItemUi(
                    exerciseId = it.id,
                    name = it.name,
                    muscleGroup = it.muscleGroup,
                    movementPattern = it.movementPattern,
                    setSummary = getSetSummary(relation.id)
                )
            }
        }

        _items.value = uiItems
    }

    fun removeExerciseFromBlock(
        exerciseId: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (currentDayPlanIds.isEmpty()) {
                    onError("No se ha encontrado el bloque")
                    return@launch
                }

                routineRepository.removeExerciseFromBlock(
                    dayPlanIds = currentDayPlanIds,
                    exerciseId = exerciseId
                )

                reloadExercises()
                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Error al eliminar ejercicio")
            }
        }
    }

    fun registerTodayWorkout(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userUid = FirebaseAuth.getInstance().currentUser?.uid

                if (userUid.isNullOrEmpty()) {
                    onError("No hay usuario logueado")
                    return@launch
                }

                if (currentDayPlanIds.isEmpty()) {
                    onError("No se ha encontrado el bloque")
                    return@launch
                }

                val todayNumber = getTodayAsRoutineDayNumber()

                val todayPlan = currentDayPlanIds
                    .mapNotNull { routineRepository.getDayPlanById(it) }
                    .firstOrNull { it.dayOfWeek == todayNumber }

                if (todayPlan == null) {
                    onError("Este bloque no está asignado al día de hoy")
                    return@launch
                }

                val today = LocalDate.now().toString()

                val result = workoutRepository.saveWorkoutFromDayPlanSets(
                    userUid = userUid,
                    date = today,
                    dayPlanId = todayPlan.id
                )

                result.onSuccess {
                    _canRegisterWorkout.value = false
                    onSuccess()
                }.onFailure { error ->
                    onError(error.message ?: "Error al registrar entrenamiento")
                }

            } catch (e: Exception) {
                onError(e.message ?: "Error al registrar entrenamiento")
            }
        }
    }

    private suspend fun getSetSummary(routineDayExerciseId: Long): String {
        val plans = routineRepository.getSetPlansForRoutineExercise(routineDayExerciseId)

        return if (plans.isEmpty()) {
            "Sin series configuradas"
        } else {
            "${plans.size} series configuradas"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun checkTodayWorkoutState() {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid

        if (userUid.isNullOrEmpty()) {
            _canRegisterWorkout.value = false
            return
        }

        val today = LocalDate.now().toString()

        _canRegisterWorkout.value = !workoutRepository.hasWorkoutForDate(userUid, today)
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