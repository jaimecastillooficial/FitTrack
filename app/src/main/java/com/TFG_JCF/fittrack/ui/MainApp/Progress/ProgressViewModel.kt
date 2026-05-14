package com.TFG_JCF.fittrack.ui.MainApp.Progress

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.ProgressRepository
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: ProgressRepository
) : ViewModel() {

    private val userUid: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    private val _weightEntries = MutableStateFlow<List<WeightEntryEntity>>(emptyList())
    val weightEntries: StateFlow<List<WeightEntryEntity>> = _weightEntries

    private val _userProfile = MutableStateFlow<UserProfileEntity?>(null)
    val userProfile: StateFlow<UserProfileEntity?> = _userProfile

    init {
        loadProgressData()
    }

    fun loadProgressData() {
        if (userUid.isBlank()) return

        viewModelScope.launch {
            _weightEntries.value = repository.getWeightEntriesByUser(userUid)
            _userProfile.value = repository.getUserProfile(userUid)
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