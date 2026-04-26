package com.TFG_JCF.fittrack.ui.MainApp.Workout.AddRoutineExercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.ExerciseRepository
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.MovementPattern
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRoutineExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private var fullExerciseList: List<ExerciseEntity> = emptyList()
    private var currentQuery: String = ""
    private var currentPattern: MovementPattern? = null
    private val _filteredExercises = MutableLiveData<List<ExerciseEntity>>()
    val filteredExercises: LiveData<List<ExerciseEntity>> = _filteredExercises

    fun prepareExercises() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            fullExerciseList = exerciseRepository.getVisibleExercisesForUser(uid)

            applyFilters()
        }
    }

    fun filterByName(query: String) {
        currentQuery = query
        applyFilters()
    }

    fun filterByPattern(pattern: MovementPattern?) {
        currentPattern = pattern
        applyFilters()
    }

    private fun applyFilters() {
        var result = fullExerciseList

        if (currentQuery.isNotBlank()) {
            result = result.filter {
                it.name.contains(currentQuery, ignoreCase = true)
            }
        }

        if (currentPattern != null) {
            result = result.filter {
                it.movementPattern == currentPattern
            }
        }

        _filteredExercises.value = result
    }

    fun addExerciseToBlock(
        dayPlanIds: List<Long>,
        exerciseId: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (dayPlanIds.isEmpty()) {
                    onError("No se ha encontrado el bloque")
                    return@launch
                }

                routineRepository.addExerciseToBlock(
                    dayPlanIds = dayPlanIds,
                    exerciseId = exerciseId
                )

                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Error al añadir ejercicio")
            }
        }
    }
}