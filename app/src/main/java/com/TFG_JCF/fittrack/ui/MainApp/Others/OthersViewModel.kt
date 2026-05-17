package com.TFG_JCF.fittrack.ui.MainApp.Others

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.UserRepository
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.ActivityLevel
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.Gender
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.GoalType
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfileEntity?>(null)
    val profile: StateFlow<UserProfileEntity?> = _profile

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return

        _email.value = firebaseUser.email.orEmpty()

        viewModelScope.launch {
            _profile.value = userRepository.getUserProfile(firebaseUser.uid)
        }
    }

    fun formatGoal(goal: GoalType): String {
        return when (goal) {
            GoalType.LOSE_WEIGHT -> "Perder peso"
            GoalType.MAINTAIN_WEIGHT -> "Mantener peso"
            GoalType.GAIN_WEIGHT -> "Ganar peso"
        }
    }

    fun formatActivityLevel(activityLevel: ActivityLevel): String {
        return when (activityLevel) {
            ActivityLevel.SEDENTARY -> "Sedentaria"
            ActivityLevel.LIGHT -> "Ligera"
            ActivityLevel.MODERATE -> "Moderada"
            ActivityLevel.HIGH -> "Alta"
            ActivityLevel.VERY_HIGH -> "Muy alta"
        }
    }

    fun formatGender(gender: Gender): String {
        return when (gender) {
            Gender.MALE -> "Masculino"
            Gender.FEMALE -> "Femenino"
        }
    }
}
