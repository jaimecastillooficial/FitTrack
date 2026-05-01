package com.TFG_JCF.fittrack.ui.MainApp.Workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.Repositories.WorkoutRepository
import com.TFG_JCF.fittrack.data.model.Workout.WorkoutHomeUiState
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
class WorkoutViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutHomeUiState())
    val uiState: StateFlow<WorkoutHomeUiState> = _uiState.asStateFlow()

    private var todayPlanId: Long? = null

    fun loadWorkoutHome() {
        viewModelScope.launch {
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid.isNullOrEmpty()) {
                todayPlanId = null
                _uiState.value = WorkoutHomeUiState(
                    activeRoutineName = "Ninguna",
                    todayWorkoutName = "-",
                    message = "No hay usuario logueado",
                    canStartWorkout = false
                )
                return@launch
            }

            val activeWeek = routineRepository.getActiveWeek(userUid)

            if (activeWeek == null) {
                todayPlanId = null
                _uiState.value = WorkoutHomeUiState(
                    activeRoutineName = "Ninguna",
                    todayWorkoutName = "-",
                    message = "Crea o activa una rutina para empezar",
                    canStartWorkout = false
                )
                return@launch
            }

            val todayNumber = getTodayAsRoutineDayNumber()

            val todayPlan = routineRepository.getDayByWeekAndNumber(
                routineWeekId = activeWeek.id,
                dayOfWeek = todayNumber
            )

            if (todayPlan == null) {
                todayPlanId = null
                _uiState.value = WorkoutHomeUiState(
                    activeRoutineName = activeWeek.name,
                    todayWorkoutName = "Descanso",
                    message = "Hoy no tienes ningún día asignado en la rutina",
                    canStartWorkout = false
                )
                return@launch
            }

            val today = LocalDate.now().toString()
            val alreadyRegistered = workoutRepository.hasWorkoutForDate(userUid, today)

            todayPlanId = if (alreadyRegistered) null else todayPlan.id

            _uiState.value = WorkoutHomeUiState(
                activeRoutineName = activeWeek.name,
                todayWorkoutName = todayPlan.dayName,
                message = if (alreadyRegistered) {
                    "Ya has registrado el entrenamiento de hoy"
                } else {
                    "Todo listo para registrar el entreno de hoy"
                },
                canStartWorkout = !alreadyRegistered
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerTodayWorkout(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid.isNullOrEmpty()) {
                onError("No hay usuario logueado")
                return@launch
            }

            val planId = todayPlanId

            if (planId == null) {
                onError("No hay ningún entrenamiento disponible para registrar hoy")
                return@launch
            }

            val today = LocalDate.now().toString()

            val result = workoutRepository.saveWorkoutFromDayPlanSets(
                userUid = userUid,
                date = today,
                dayPlanId = planId
            )

            result.onSuccess {
                loadWorkoutHome()
                onSuccess()
            }.onFailure { error ->
                onError(error.message ?: "Error al registrar entrenamiento")
            }
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