package com.TFG_JCF.fittrack.ui.Auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.UserRepository
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.model.SignUpData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){

    val signUpData = SignUpData()

    fun createUserProfile(){

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

        }
    }
}