package com.TFG_JCF.fittrack.ui.MainApp.Workout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentWorkoutBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineList.RoutineListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutFragment : Fragment(R.layout.fragment_workout) {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWorkoutBinding.bind(view)

        initListeners()
        observeViewModel()
        viewModel.loadWorkoutHome()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadWorkoutHome()
    }

    private fun initListeners() {
        binding.btnStartWorkout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Ir a WorkoutSessionFragment",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnMyRoutines.setOnClickListener {
            val intent = Intent(requireContext(), RoutineListActivity::class.java)
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Ir a WorkoutHistoryFragment",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.tvActiveRoutineValue.text = state.activeRoutineName
                binding.tvTodayValue.text = state.todayWorkoutName
                binding.tvWorkoutMessage.text = state.message
                binding.btnStartWorkout.isEnabled = state.canStartWorkout
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}