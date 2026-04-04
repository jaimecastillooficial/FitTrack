package com.TFG_JCF.fittrack.ui.Auth.SessionCheck


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionCheckViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun checkSessionAndProfile(onResult: (SessionDestination) -> Unit) {
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                onResult(SessionDestination.LOGIN)
                return@launch
            }

            val profile = userRepository.getUserProfile(user.uid)

            if (profile != null) {
                onResult(SessionDestination.HOME)
            } else {
                onResult(SessionDestination.ONBOARDING)
            }
        }
    }
}