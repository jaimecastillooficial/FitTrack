package com.TFG_JCF.fittrack.ui.MainApp.Workout.WorkoutHistory

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.data.model.Workout.WorkoutHistoryItemUi
import com.TFG_JCF.fittrack.databinding.ActivityWorkoutHistoryBinding
import com.TFG_JCF.fittrack.databinding.DialogDeleteWorkoutBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.WorkoutHistory.adapter.WorkoutHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutHistoryBinding
    private val viewModel: WorkoutHistoryViewModel by viewModels()
    private lateinit var historyAdapter: WorkoutHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        initListeners()
        observeViewModel()

        viewModel.loadHistory()
    }

    override fun onResume() {3
        super.onResume()
        viewModel.loadHistory()
    }

    private fun setupRecycler() {
        historyAdapter = WorkoutHistoryAdapter(
            onDeleteClick = { workout ->
                showDeleteWorkoutDialog(workout)
            }
        )

        binding.rvWorkoutHistory.apply {
            layoutManager = LinearLayoutManager(this@WorkoutHistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.workouts.collect { workouts ->
                historyAdapter.submitList(workouts)
                binding.tvEmptyHistory.isVisible = workouts.isEmpty()
            }
        }
    }

    private fun showDeleteWorkoutDialog(workout: WorkoutHistoryItemUi) {
        val dialogBinding = DialogDeleteWorkoutBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.tvDeleteWorkoutMessage.text =
            "¿Quieres eliminar el entrenamiento ${workout.dayName} del día ${workout.date}?"

        dialogBinding.btnCancelDeleteWorkout.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnConfirmDeleteWorkout.setOnClickListener {
            viewModel.deleteWorkout(
                workout = workout,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Entrenamiento eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onError = { message ->
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }
}