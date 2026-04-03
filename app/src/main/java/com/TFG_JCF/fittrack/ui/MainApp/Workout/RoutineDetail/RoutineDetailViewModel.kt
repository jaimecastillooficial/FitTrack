package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayExerciseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {

    private val _exercises = MutableStateFlow<List<RoutineDayExerciseEntity>>(emptyList())
    val exercises: StateFlow<List<RoutineDayExerciseEntity>> = _exercises

    fun loadExercises(routineId: Long) {
        viewModelScope.launch {
//            _exercises.value = repository.getExercisesByRoutine(routineId)
        }
    }
}