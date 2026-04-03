package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineList


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.model.Workout.RoutineListItemUi
import com.TFG_JCF.fittrack.data.model.Workout.RoutineListUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _routines = MutableStateFlow<List<RoutineListItemUi>>(emptyList())
    val routines: StateFlow<List<RoutineListItemUi>> = _routines.asStateFlow()

    fun loadRoutines() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            val routines = routineRepository.getAllWeeks(uid)

            _routines.value = routines.map { routine ->
                RoutineListItemUi(
                     routine.id,
                     routine.name,
                     routine.isActive
                )
            }
        }
    }

    fun createRoutine(name: String) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            if (name.isBlank()) return@launch

            routineRepository.createRoutineWeek(
                 uid,
                 name.trim()
            )

            loadRoutines()
        }
    }

    fun setActiveRoutine(routineId: Long) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            routineRepository.setActiveWeek(uid, routineId)
            loadRoutines()
        }
    }
    fun deleteRoutine(routineId:Long){
        viewModelScope.launch {
            val routine = routineRepository.getRoutineWeekById(routineId) ?: return@launch

            routineRepository.deleteRoutineWeek(routine)

            loadRoutines()
        }
    }
}