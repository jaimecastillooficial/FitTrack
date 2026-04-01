package com.TFG_JCF.fittrack.ui.MainApp.Home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        observeViewModel()
        viewModel.loadHomeData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.loadHomeData()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {

        // Calorias restantes
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesRemaining.collect { remaining ->
                binding.tvCaloriesValue.text = remaining
            }
        }
        // Calorias consumidas
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesConsumed.collect { consumed ->
                binding.tvCaloriesLeft.text = "$consumed"
            }
        }
        // Calorias objetivo

        //Barra de progreso
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.progressCalories.collect { progress ->
                binding.progressCalories.progress = progress
            }
        }
        //Proteinas consumidas
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.proteinConsumed.collect {
                binding.tvProteinsValue.text =
                    "${viewModel.proteinConsumed.value} / ${viewModel.proteinGoal.value}g"
            }
        }
        //Proteinas Objetivo
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.proteinGoal.collect {
                binding.tvProteinsValue.text =
                    "${viewModel.proteinConsumed.value} / ${viewModel.proteinGoal.value}g"
            }
        }
        //Carbs consumidas
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.carbsConsumed.collect {
                binding.tvCarbsValue.text =
                    "${viewModel.carbsConsumed.value} / ${viewModel.carbsGoal.value}g"
            }
        }
        //Carbs Objetivo
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.carbsGoal.collect {
                binding.tvCarbsValue.text =
                    "${viewModel.carbsConsumed.value} / ${viewModel.carbsGoal.value}g"
            }
        }
        //Grasas consumidas
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fatsConsumed.collect {
                binding.tvFatsValue.text =
                    "${viewModel.fatsConsumed.value} / ${viewModel.fatsGoal.value}g"
            }
        }
        //Grasas Objetivo
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fatsGoal.collect {
                binding.tvFatsValue.text =
                    "${viewModel.fatsConsumed.value} / ${viewModel.fatsGoal.value}g"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}