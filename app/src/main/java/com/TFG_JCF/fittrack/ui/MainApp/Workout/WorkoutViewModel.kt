package com.TFG_JCF.fittrack.ui.MainApp.Workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.model.Workout.WorkoutHomeUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutHomeUiState())
    val uiState: StateFlow<WorkoutHomeUiState> = _uiState.asStateFlow()

    fun loadWorkoutHome() {
        viewModelScope.launch {
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid.isNullOrEmpty()) {
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
                _uiState.value = WorkoutHomeUiState(
                    activeRoutineName = activeWeek.name,
                    todayWorkoutName = "Descanso",
                    message = "Hoy no tienes ningún día asignado en la rutina",
                    canStartWorkout = false
                )
                return@launch
            }

            _uiState.value = WorkoutHomeUiState(
                activeRoutineName = activeWeek.name,
                todayWorkoutName = todayPlan.dayName,
                message = "Todo listo para entrenar",
                canStartWorkout = true
            )
        }
    }

    /**
     * 1 = lunes
     * 2 = martes
     * 3 = miércoles
     * 4 = jueves
     * 5 = viernes
     * 6 = sábado
     * 7 = domingo
     */
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