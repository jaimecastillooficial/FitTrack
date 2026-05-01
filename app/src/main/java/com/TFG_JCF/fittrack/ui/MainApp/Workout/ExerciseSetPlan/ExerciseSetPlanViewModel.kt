package com.TFG_JCF.fittrack.ui.MainApp.Workout.ExerciseSetPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineExerciseSetPlanEntity
import com.TFG_JCF.fittrack.data.model.Workout.SetPlanInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSetPlanViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _sets = MutableStateFlow<List<SetPlanInput>>(listOf(SetPlanInput()))
    val sets: StateFlow<List<SetPlanInput>> = _sets.asStateFlow()

    private val currentSets = mutableListOf(SetPlanInput())

    private var dayPlanIds: List<Long> = emptyList()
    private var exerciseId: Long = -1L

    fun load(dayPlanIds: List<Long>, exerciseId: Long) {
        this.dayPlanIds = dayPlanIds
        this.exerciseId = exerciseId

        viewModelScope.launch {
            val plans = routineRepository.getSetPlansForExerciseInBlock(dayPlanIds, exerciseId)

            val loadedSets = if (plans.isEmpty()) {
                listOf(SetPlanInput())
            } else {
                plans.map { plan ->
                    SetPlanInput(
                        weightText = plan.plannedWeightKg?.toString().orEmpty(),
                        repsText = plan.plannedReps.toString(),
                        rirText = plan.plannedRir?.toString().orEmpty()
                    )
                }
            }

            currentSets.clear()
            currentSets.addAll(loadedSets)

            _sets.value = currentSets.map { it.copy() }
        }
    }

    fun addEmptySet() {
        currentSets.add(SetPlanInput())
        _sets.value = currentSets.map { it.copy() }
    }

    fun removeSet(position: Int) {
        if (position !in currentSets.indices) return

        currentSets.removeAt(position)

        if (currentSets.isEmpty()) {
            currentSets.add(SetPlanInput())
        }

        _sets.value = currentSets.map { it.copy() }
    }

    fun updateSet(position: Int, updated: SetPlanInput) {
        if (position !in currentSets.indices) return

        currentSets[position] = updated
    }

    fun save(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val validatedPlans = validateAndBuildPlans()

                routineRepository.saveSetPlansForExerciseInBlock(
                    dayPlanIds = dayPlanIds,
                    exerciseId = exerciseId,
                    setPlans = validatedPlans
                )

                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Error al guardar las series")
            }
        }
    }

    private fun validateAndBuildPlans(): List<RoutineExerciseSetPlanEntity> {
        if (dayPlanIds.isEmpty() || exerciseId <= 0) {
            throw IllegalArgumentException("No se ha encontrado el ejercicio")
        }

        val current = currentSets.toList()

        if (current.isEmpty()) {
            throw IllegalArgumentException("Añade al menos una serie")
        }

        return current.mapIndexed { index, input ->
            val weightText = input.weightText.trim().replace(',', '.')
            val repsText = input.repsText.trim()
            val rirText = input.rirText.trim()

            if (weightText.isBlank() && repsText.isBlank() && rirText.isBlank()) {
                throw IllegalArgumentException("La serie ${index + 1} está vacía")
            }

            val reps = repsText.toIntOrNull()
                ?: throw IllegalArgumentException("La serie ${index + 1} necesita repeticiones")

            if (reps <= 0) {
                throw IllegalArgumentException(
                    "Las repeticiones de la serie ${index + 1} deben ser mayores que 0"
                )
            }

            val weight = if (weightText.isBlank()) {
                null
            } else {
                weightText.toFloatOrNull()
                    ?: throw IllegalArgumentException("El peso de la serie ${index + 1} no es válido")
            }

            if (weight != null && weight < 0f) {
                throw IllegalArgumentException("El peso de la serie ${index + 1} no puede ser negativo")
            }

            val rir = if (rirText.isBlank()) {
                null
            } else {
                rirText.toIntOrNull()
                    ?: throw IllegalArgumentException("El RIR de la serie ${index + 1} no es válido")
            }

            if (rir != null && (rir < 0 || rir > 10)) {
                throw IllegalArgumentException("El RIR de la serie ${index + 1} debe estar entre 0 y 10")
            }

            RoutineExerciseSetPlanEntity(
                routineDayExerciseId = 0,
                setNumber = index + 1,
                plannedWeightKg = weight,
                plannedReps = reps,
                plannedRir = rir
            )
        }
    }
}