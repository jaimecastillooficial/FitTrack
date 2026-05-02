package com.TFG_JCF.fittrack.ui.MainApp.Workout

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.DialogRegisterWorkoutBinding
import com.TFG_JCF.fittrack.databinding.FragmentWorkoutBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineList.RoutineListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.graphics.drawable.toDrawable

@AndroidEntryPoint
class WorkoutFragment : Fragment(R.layout.fragment_workout) {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        binding.btnStartWorkout.setOnClickListener {
            showRegisterWorkoutDialog()
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
                binding.btnStartWorkout.text = if (state.canStartWorkout) {
                    "Registrar entreno"
                } else {
                    "Entreno no disponible"
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showRegisterWorkoutDialog() {
        val dialogBinding = DialogRegisterWorkoutBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnRegister.setOnClickListener {
            dialog.dismiss()

            viewModel.registerTodayWorkout(
                onSuccess = {
                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Entrenamiento registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onError = { message ->
                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}