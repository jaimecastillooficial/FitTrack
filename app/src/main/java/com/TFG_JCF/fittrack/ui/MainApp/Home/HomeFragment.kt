package com.TFG_JCF.fittrack.ui.MainApp.Home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.TFG_JCF.fittrack.databinding.FragmentHomeBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesRemaining.collect { remaining ->
                binding.tvCaloriesValue.text = remaining
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesConsumed.collect { consumed ->
                binding.tvCaloriesLeft.text = "$consumed"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.progressCalories.collect { progress ->
                binding.progressCalories.progress = progress
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.proteinConsumed.collect {
                binding.tvProteinsValue.text =
                    "${viewModel.proteinConsumed.value} / ${viewModel.proteinGoal.value}g"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.proteinGoal.collect {
                binding.tvProteinsValue.text =
                    "${viewModel.proteinConsumed.value} / ${viewModel.proteinGoal.value}g"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.carbsConsumed.collect {
                binding.tvCarbsValue.text =
                    "${viewModel.carbsConsumed.value} / ${viewModel.carbsGoal.value}g"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.carbsGoal.collect {
                binding.tvCarbsValue.text =
                    "${viewModel.carbsConsumed.value} / ${viewModel.carbsGoal.value}g"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fatsConsumed.collect {
                binding.tvFatsValue.text =
                    "${viewModel.fatsConsumed.value} / ${viewModel.fatsGoal.value}g"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fatsGoal.collect {
                binding.tvFatsValue.text =
                    "${viewModel.fatsConsumed.value} / ${viewModel.fatsGoal.value}g"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayWorkoutName.collect { dayName ->
                binding.tvWorkoutDay.text = dayName
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todayWorkoutExercises.collect { text ->
                binding.tvExercises.text = text
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lastWorkoutName.collect { dayName ->
                binding.tvLastWorkoutDay.text = dayName
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lastWorkoutDate.collect { dateText ->
                binding.tvWhen.text = dateText
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weightEntries.collect { entries ->
                updateWeightChart(entries)
            }
        }
    }

    private fun updateWeightChart(entries: List<WeightEntryEntity>) {
        if (entries.isEmpty()) {
            binding.lineChartWeightHome.clear()
            binding.lineChartWeightHome.invalidate()
            binding.tvWeightChartInfo.text = "Sin registros de peso"
            return
        }

        val lastWeight = entries.last().weightKg
        binding.tvWeightChartInfo.text = "Peso actual: ${lastWeight} kg"

        val chartEntries = entries.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.weightKg)
        }

        val dataSet = LineDataSet(chartEntries, "Peso")

        dataSet.lineWidth = 2.5f
        dataSet.circleRadius = 4f
        dataSet.valueTextSize = 9f
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.ft_primary)
        dataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.ft_primary))

        binding.lineChartWeightHome.data = LineData(dataSet)

        binding.lineChartWeightHome.description.isEnabled = false
        binding.lineChartWeightHome.legend.isEnabled = false
        binding.lineChartWeightHome.axisRight.isEnabled = false

        binding.lineChartWeightHome.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChartWeightHome.xAxis.granularity = 1f

        binding.lineChartWeightHome.animateX(300)
        binding.lineChartWeightHome.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}