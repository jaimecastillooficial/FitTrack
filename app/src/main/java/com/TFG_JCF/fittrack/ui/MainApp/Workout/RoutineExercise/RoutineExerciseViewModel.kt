package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.ExerciseRepository
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.model.Workout.RoutineExerciseItemUi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineExerciseViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _blockTitle = MutableStateFlow("")
    val blockTitle: StateFlow<String> = _blockTitle.asStateFlow()

    private val _items = MutableStateFlow<List<RoutineExerciseItemUi>>(emptyList())
    val items: StateFlow<List<RoutineExerciseItemUi>> = _items.asStateFlow()

    private var currentDayPlanIds: List<Long> = emptyList()

    fun loadBlockExercises(
        blockTitle: String,
        dayPlanIds: List<Long>
    ) {
        viewModelScope.launch {
            _blockTitle.value = blockTitle
            currentDayPlanIds = dayPlanIds

            reloadExercises()
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
                    movementPattern = it.movementPattern
                )
            }
        }

        _items.value = uiItems
    }
    fun reloadCurrentBlock() {
        viewModelScope.launch {
            reloadExercises()
        }
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

}