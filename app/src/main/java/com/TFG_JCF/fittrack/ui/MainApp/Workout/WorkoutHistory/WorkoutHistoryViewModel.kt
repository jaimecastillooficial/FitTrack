package com.TFG_JCF.fittrack.ui.MainApp.Workout.WorkoutHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.WorkoutRepository
import com.TFG_JCF.fittrack.data.model.Workout.WorkoutHistoryItemUi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutHistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _workouts = MutableStateFlow<List<WorkoutHistoryItemUi>>(emptyList())
    val workouts: StateFlow<List<WorkoutHistoryItemUi>> = _workouts.asStateFlow()

    fun loadHistory() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            val history = workoutRepository.getWorkoutHistory(uid)

            _workouts.value = history.map { workoutFull ->
                val orderedExercises = workoutFull.exercises.sortedBy { it.workoutExercise.orderIndex }
                val totalSets = orderedExercises.sumOf { it.sets.size }

                WorkoutHistoryItemUi(
                    id = workoutFull.workout.id,
                    date = workoutFull.workout.date,
                    dayName = workoutFull.workout.dayName,
                    exerciseCount = orderedExercises.size,
                    setCount = totalSets,
                    detailText = buildDetailText(orderedExercises)
                )
            }
        }
    }

    private fun buildDetailText(
        exercises: List<com.TFG_JCF.fittrack.data.database.Relations.Workout.WorkoutExerciseFull>
    ): String {
        return exercises.joinToString(separator = "\n\n") { exerciseFull ->
            val setsText = exerciseFull.sets
                .sortedBy { it.setNumber }
                .joinToString(separator = "\n") { set ->
                    val weight = set.weightKg?.let { "${it} kg" } ?: "sin peso"
                    val reps = set.reps?.let { "${it} reps" } ?: "sin reps"
                    val rir = set.rir?.let { "RIR ${it}" } ?: "sin RIR"
                    "Serie ${set.setNumber}: $weight · $reps · $rir"
                }

            "${exerciseFull.exercise.name}\n$setsText"
        }
    }
    fun deleteWorkout(
        workout: WorkoutHistoryItemUi,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid

            if (uid.isNullOrEmpty()) {
                onError("No hay usuario logueado")
                return@launch
            }

            try {
                workoutRepository.deleteWorkoutById(
                    userUid = uid,
                    workoutId = workout.id
                )

                loadHistory()
                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Error al eliminar entrenamiento")
            }
        }
    }
}
