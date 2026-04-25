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
    private var alreadyAddedIds: Set<Long> = emptySet()

    private var currentQuery: String = ""
    private var currentPattern: MovementPattern? = null

    private val _filteredExercises = MutableLiveData<List<ExerciseEntity>>()
    val filteredExercises: LiveData<List<ExerciseEntity>> = _filteredExercises

    val defaultExercises = listOf(
        ExerciseEntity(name = "Press banca", muscleGroup = "Pecho", movementPattern = MovementPattern.PUSH, isPublic = true),
        ExerciseEntity(name = "Press inclinado con mancuernas", muscleGroup = "Pecho", movementPattern = MovementPattern.PUSH, isPublic = true),
        ExerciseEntity(name = "Fondos en paralelas", muscleGroup = "Pecho / Tríceps", movementPattern = MovementPattern.PUSH, isPublic = true),
        ExerciseEntity(name = "Press militar", muscleGroup = "Hombro", movementPattern = MovementPattern.PUSH, isPublic = true),
        ExerciseEntity(name = "Elevaciones laterales", muscleGroup = "Hombro", movementPattern = MovementPattern.PUSH, isPublic = true),
        ExerciseEntity(name = "Extensión de tríceps en polea", muscleGroup = "Tríceps", movementPattern = MovementPattern.PUSH, isPublic = true),

        ExerciseEntity(name = "Dominadas", muscleGroup = "Espalda", movementPattern = MovementPattern.PULL, isPublic = true),
        ExerciseEntity(name = "Dominadas supinas", muscleGroup = "Espalda / Bíceps", movementPattern = MovementPattern.PULL, isPublic = true),
        ExerciseEntity(name = "Remo con barra", muscleGroup = "Espalda", movementPattern = MovementPattern.PULL, isPublic = true),
        ExerciseEntity(name = "Remo unilateral con mancuerna", muscleGroup = "Espalda", movementPattern = MovementPattern.PULL, isPublic = true),
        ExerciseEntity(name = "Jalón al pecho", muscleGroup = "Espalda", movementPattern = MovementPattern.PULL, isPublic = true),
        ExerciseEntity(name = "Face pull", muscleGroup = "Espalda alta / Hombro posterior", movementPattern = MovementPattern.PULL, isPublic = true),
        ExerciseEntity(name = "Curl bíceps con barra", muscleGroup = "Bíceps", movementPattern = MovementPattern.PULL, isPublic = true),
        ExerciseEntity(name = "Curl martillo", muscleGroup = "Bíceps", movementPattern = MovementPattern.PULL, isPublic = true),

        ExerciseEntity(name = "Sentadilla", muscleGroup = "Pierna", movementPattern = MovementPattern.LEGS, isPublic = true),
        ExerciseEntity(name = "Prensa de piernas", muscleGroup = "Pierna", movementPattern = MovementPattern.LEGS, isPublic = true),
        ExerciseEntity(name = "Peso muerto rumano", muscleGroup = "Femoral / Glúteo", movementPattern = MovementPattern.LEGS, isPublic = true),
        ExerciseEntity(name = "Zancadas", muscleGroup = "Pierna / Glúteo", movementPattern = MovementPattern.LEGS, isPublic = true),
        ExerciseEntity(name = "Curl femoral", muscleGroup = "Femoral", movementPattern = MovementPattern.LEGS, isPublic = true),
        ExerciseEntity(name = "Extensión de cuádriceps", muscleGroup = "Cuádriceps", movementPattern = MovementPattern.LEGS, isPublic = true),
        ExerciseEntity(name = "Elevación de gemelos", muscleGroup = "Gemelo", movementPattern = MovementPattern.LEGS, isPublic = true),

        ExerciseEntity(name = "Plancha", muscleGroup = "Core", movementPattern = MovementPattern.CORE, isPublic = true),
        ExerciseEntity(name = "Elevaciones de piernas", muscleGroup = "Core", movementPattern = MovementPattern.CORE, isPublic = true),
        ExerciseEntity(name = "Ab wheel", muscleGroup = "Core", movementPattern = MovementPattern.CORE, isPublic = true),
        ExerciseEntity(name = "Russian twists", muscleGroup = "Core", movementPattern = MovementPattern.CORE, isPublic = true),
        ExerciseEntity(name = "Pallof press", muscleGroup = "Core", movementPattern = MovementPattern.CORE, isPublic = true),
        ExerciseEntity(name = "Dragon flag", muscleGroup = "Core", movementPattern = MovementPattern.CORE, isPublic = true)
    )

    fun prepareExercises(dayPlanIds: List<Long>) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            exerciseRepository.prepareDefaultExercises(defaultExercises)

            alreadyAddedIds = if (dayPlanIds.isNotEmpty()) {
                routineRepository.getExercisesForDay(dayPlanIds.first())
                    .map { it.exerciseId }
                    .toSet()
            } else {
                emptySet()
            }

            fullExerciseList = exerciseRepository.getVisibleExercisesForUser(uid)
                .filter { it.id !in alreadyAddedIds }

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