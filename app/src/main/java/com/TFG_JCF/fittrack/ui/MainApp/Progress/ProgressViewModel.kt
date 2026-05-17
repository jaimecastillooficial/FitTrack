package com.TFG_JCF.fittrack.ui.MainApp.Progress

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.ProgressRepository
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.TFG_JCF.fittrack.data.model.Progress.DietCaloriesChart
import com.TFG_JCF.fittrack.data.model.Progress.WorkoutWeekChart
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: ProgressRepository
) : ViewModel() {

    private val userUid: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    private val _weightEntries = MutableStateFlow<List<WeightEntryEntity>>(emptyList())
    val weightEntries: StateFlow<List<WeightEntryEntity>> = _weightEntries

    private val _userProfile = MutableStateFlow<UserProfileEntity?>(null)
    val userProfile: StateFlow<UserProfileEntity?> = _userProfile

    private val _dietCalories = MutableStateFlow<List<DietCaloriesChart>>(emptyList())
    val dietCalories: StateFlow<List<DietCaloriesChart>> = _dietCalories

    private val _workoutWeeks = MutableStateFlow<List<WorkoutWeekChart>>(emptyList())
    val workoutWeeks: StateFlow<List<WorkoutWeekChart>> = _workoutWeeks

    init {
        loadProgressData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadProgressData() {
        if (userUid.isBlank()) return

        viewModelScope.launch {
            _weightEntries.value = repository.getWeightEntriesByUser(userUid)
            _userProfile.value = repository.getUserProfile(userUid)
            _dietCalories.value = repository.getCaloriesLast7Days(userUid)
            _workoutWeeks.value = repository.getWorkoutWeeksLast4Weeks(userUid)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addWeight(weightKg: Float, date: LocalDate = LocalDate.now()) {
        if (userUid.isBlank()) return

        viewModelScope.launch {
            val entry = WeightEntryEntity(
                userUid = userUid,
                date = date.toString(),
                weightKg = weightKg
            )

            repository.insertWeightEntry(entry)

            // Recargamos después de borrar para actualizar el Fragment.
            loadProgressData()
        }
    }

    fun deleteWeightEntry(entry: WeightEntryEntity) {
        viewModelScope.launch {
            repository.deleteWeightEntry(entry)

            // Recargamos después de borrar para actualizar el Fragment.
            loadProgressData()
        }
    }
}