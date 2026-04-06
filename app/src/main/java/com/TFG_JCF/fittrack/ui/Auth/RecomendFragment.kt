package com.TFG_JCF.fittrack.ui.Auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentRecomendBinding
import com.TFG_JCF.fittrack.ui.MainApp.Home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecomendFragment : Fragment() {

    private var _binding: FragmentRecomendBinding? = null
    private val binding get() = _binding!!

    private val vm: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecomendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        vm.calculateRecommendation()
        initUI()
    }

    private fun initUI() {

        updateButtonState()

        // Escuchar cambios en el texto del campos de texto
        binding.txtCaloriesGoal.addTextChangedListener {
            updateButtonState()
        }

        binding.txtTargetWeight.addTextChangedListener {
            updateButtonState()
        }
        binding.btnFinish.setOnClickListener {
            // Si está "deshabilitado", no hace nada
            if (!binding.btnFinish.isEnabled) return@setOnClickListener

            val calories = binding.txtCaloriesGoal.text.toString().toInt()
            val targetWeight = binding.txtTargetWeight.text.toString().toFloat()


            vm.saveRecommendationData(calories, targetWeight)
            vm.createUserProfile()
        }
        binding.goBack.setOnClickListener {
           navigateTophysicalAspects()
        }
    }

    // Actualiza el estado del botón según los valores de los input ademas de comprabar valores minimos para evitar cantidades absurdas
    //Se ha hecho utilizando un comoponente llamado selector ubicado en la carpeta colors que si enabled = true es de un color y si es false es de otro
    private fun updateButtonState() {
        val calories = binding.txtCaloriesGoal.text.toString().toIntOrNull()
        val weight = binding.txtTargetWeight.text.toString().toFloatOrNull()


        val isValid = calories != null && calories > 1000 &&
                weight != null && weight > 30

        binding.btnFinish.isEnabled = isValid

    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.recommendation.collect { recommendation ->
                if (recommendation != null) {
                    binding.tvRecommendedCalories.text =
                        "${recommendation.recommendedCalories} kcal"

                    binding.tvRecommendedTargetWeight.text =
                        "${recommendation.recommendedTargetWeight} kg"

                    if (binding.txtCaloriesGoal.text.isNullOrBlank()) {
                        binding.txtCaloriesGoal.setText(
                            recommendation.recommendedCalories.toString()
                        )
                    }

                    if (binding.txtTargetWeight.text.isNullOrBlank()) {
                        binding.txtTargetWeight.setText(
                            recommendation.recommendedTargetWeight.toString()
                        )
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.profileCreated.collect { created ->
                if (created) {
                    vm.resetProfileCreatedFlag()
                    navigateToHome()
                }
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    private fun navigateTophysicalAspects(){
        findNavController().navigate(R.id.action_recommend_to_physical)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}