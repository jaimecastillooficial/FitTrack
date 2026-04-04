package com.TFG_JCF.fittrack.ui.Auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.UserRepository
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.model.Diet.CalorieRecomendation
import com.TFG_JCF.fittrack.data.model.SignUpData
import com.TFG_JCF.fittrack.data.utils.RecommendationCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val signUpData = SignUpData()

    private val _recommendation = MutableStateFlow<CalorieRecomendation?>(null)
    val recommendation: StateFlow<CalorieRecomendation?> = _recommendation

    private val _profileCreated = MutableStateFlow(false)
    val profileCreated: StateFlow<Boolean> = _profileCreated

    fun calculateRecommendation() {
        val data = signUpData

        val gender = data.gender ?: return
        val age = data.age ?: return
        val height = data.height ?: return
        val weight = data.weight ?: return
        val activityLevel = data.activityLevel ?: return
        val goalType = data.goalType ?: return

        _recommendation.value = RecommendationCalculator.calculateRecommendation(
            gender = gender,
            age = age,
            height = height,
            weight = weight,
            activityLevel = activityLevel,
            goalType = goalType
        )
    }

    fun saveRecommendationData(calories: Int, targetWeight: Float) {
        signUpData.dailyCaloriesGoal = calories
        signUpData.targetWeight = targetWeight
    }

    fun createUserProfile() {
        val data = signUpData

        val profile = UserProfileEntity(
            uid = data.uid!!,
            name = data.name!!,
            gender = data.gender!!,
            heightCm = data.height!!,
            age = data.age!!,
            goalType = data.goalType!!,
            activityLevel = data.activityLevel!!,
            currentWeight = data.weight!!,
            targetWeight = data.targetWeight,
            dailyCaloriesGoal = data.dailyCaloriesGoal
        )

        viewModelScope.launch {
            userRepository.insertUserProfile(profile)

            val user = userRepository.getUserProfile(profile.uid)
            Log.d("FitTrack_DB", "Usuario leído de BD: $user")

            _profileCreated.value = true
        }
    }

    fun resetProfileCreatedFlag() {
        _profileCreated.value = false
    }


}