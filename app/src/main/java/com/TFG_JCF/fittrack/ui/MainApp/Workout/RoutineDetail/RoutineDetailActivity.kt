package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.ActivityRoutineDetailBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.adapter.RoutineDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoutineDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutineDetailBinding
    private val viewModel: RoutineDetailViewModel by viewModels()

    private lateinit var detailAdapter: RoutineDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val routineId = intent.getLongExtra(EXTRA_ROUTINE_ID, -1L)

        if (routineId == -1L) {
            finish()
            return
        }

        setupRecycler()
        initListeners()
        observeViewModel()

        viewModel.loadRoutineDetail(routineId)
    }

    private fun setupRecycler() {
        detailAdapter = RoutineDetailAdapter(
            onViewExercisesClick = { item ->
                Toast.makeText(
                    this,
                    "Luego abrimos ejercicios de ${item.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        binding.rvRoutineBlocks.apply {
            layoutManager = LinearLayoutManager(this@RoutineDetailActivity)
            adapter = detailAdapter
        }
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.routineName.collect { routineName ->
                binding.tvRoutineTitle.text = routineName
            }
        }

        lifecycleScope.launch {
            viewModel.items.collect { items ->
                detailAdapter.submitList(items)
                binding.tvEmptyBlocks.isVisible = items.isEmpty()
            }
        }
    }

    companion object {
        const val EXTRA_ROUTINE_ID = "routine_id"

        fun createIntent(
            activity: AppCompatActivity,
            routineId: Long
        ): Intent {
            return Intent(activity, RoutineDetailActivity::class.java).apply {
                putExtra(EXTRA_ROUTINE_ID, routineId)
            }
        }
    }
}

