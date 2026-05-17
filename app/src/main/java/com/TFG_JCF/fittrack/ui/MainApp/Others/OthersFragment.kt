package com.TFG_JCF.fittrack.ui.MainApp.Others

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.BuildConfig
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.databinding.FragmentOthersBinding
import com.TFG_JCF.fittrack.ui.Auth.SignUpActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OthersFragment : Fragment(R.layout.fragment_others) {

    private var _binding: FragmentOthersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OthersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOthersBinding.bind(view)

        initListeners()
        observeViewModel()
    }

    private fun initListeners() {
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.cardRecommendations.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Las recomendaciones se podrán ampliar más adelante",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profile.collect { profile ->
                if (profile != null) {
                    showProfile(profile)
                } else {
                    showEmptyProfile()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.email.collect { email ->
                binding.tvEmailValue.text = email.ifBlank { "Sin correo" }
            }
        }
    }

    private fun showProfile(profile: UserProfileEntity) {
        binding.tvNameValue.text = profile.name
        binding.tvAgeValue.text = "${profile.age} años"
        binding.tvHeightValue.text = "${profile.heightCm} cm"
        binding.tvCurrentWeightValue.text = "${profile.currentWeight} kg"
        binding.tvTargetWeightValue.text = profile.targetWeight?.let { "${it} kg" } ?: "Sin objetivo"
        binding.tvGoalValue.text = viewModel.formatGoal(profile.goalType)
        binding.tvActivityValue.text = viewModel.formatActivityLevel(profile.activityLevel)
        binding.tvGenderValue.text = viewModel.formatGender(profile.gender)
        binding.tvCaloriesValue.text = profile.dailyCaloriesGoal?.let { "$it kcal" } ?: "Sin calcular"

        binding.tvProfileSubtitle.text = "Datos usados para calcular dieta y progreso"
        binding.tvRecommendationsText.text = buildRecommendationText(profile)
        binding.tvVersionValue.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }

    private fun showEmptyProfile() {
        binding.tvNameValue.text = "Sin perfil"
        binding.tvProfileSubtitle.text = "No se han encontrado datos del usuario"
        binding.tvRecommendationsText.text = "Completa el perfil para ver recomendaciones básicas."
        binding.tvVersionValue.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }

    private fun buildRecommendationText(profile: UserProfileEntity): String {
        val calories = profile.dailyCaloriesGoal?.let { "$it kcal/día" } ?: "calorías no calculadas"
        val targetWeight = profile.targetWeight?.let { "$it kg" } ?: "sin peso objetivo"

        return "Objetivo: ${viewModel.formatGoal(profile.goalType)} \nIngesta diaria: $calories \nPeso objetivo: $targetWeight"
    }

    private fun showLogoutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val btnCancelLogout = dialogView.findViewById<MaterialButton>(R.id.btnCancelLogout)
        val btnConfirmLogout = dialogView.findViewById<MaterialButton>(R.id.btnConfirmLogout)

        btnCancelLogout.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmLogout.setOnClickListener {
            dialog.dismiss()
            cerrarSesion()
        }

        dialog.show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireContext(), SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUserProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
