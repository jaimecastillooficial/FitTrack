package com.TFG_JCF.fittrack.ui.MainApp.Workout.WorkoutHistory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.databinding.ActivityWorkoutHistoryBinding
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

    override fun onResume() {
        super.onResume()
        viewModel.loadHistory()
    }

    private fun setupRecycler() {
        historyAdapter = WorkoutHistoryAdapter()

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
}
