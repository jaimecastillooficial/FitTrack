package com.TFG_JCF.fittrack.ui.MainApp.Workout.ExerciseSetPlan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.databinding.ActivityExerciseSetPlanBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.ExerciseSetPlan.adapter.ExerciseSetPlanAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseSetPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseSetPlanBinding
    private val viewModel: ExerciseSetPlanViewModel by viewModels()
    private lateinit var adapter: ExerciseSetPlanAdapter

    private var exerciseName: String = ""
    private var exerciseId: Long = -1L
    private var dayPlanIds: List<Long> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseSetPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME).orEmpty()
        exerciseId = intent.getLongExtra(EXTRA_EXERCISE_ID, -1L)
        dayPlanIds = intent.getLongArrayExtra(EXTRA_DAY_PLAN_IDS)?.toList() ?: emptyList()

        if (exerciseName.isBlank() || exerciseId <= 0 || dayPlanIds.isEmpty()) {
            finish()
            return
        }

        binding.tvExerciseTitle.text = exerciseName

        initRecycler()
        initListeners()
        observeViewModel()

        viewModel.load(dayPlanIds, exerciseId)
    }

    private fun initRecycler() {
        adapter = ExerciseSetPlanAdapter(
            items = emptyList(),
            onItemChanged = { position, item ->
                if (position >= 0) {
                    viewModel.updateSet(position, item)
                }
            },
            onDeleteClick = { position ->
                if (position >= 0) {
                    viewModel.removeSet(position)
                }
            }
        )

        binding.rvSets.layoutManager = LinearLayoutManager(this)
        binding.rvSets.adapter = adapter
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddSet.setOnClickListener {
            viewModel.addEmptySet()
        }

        binding.btnSaveSets.setOnClickListener {
            viewModel.save(
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(this, "Series guardadas", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                },
                onError = { message ->
                    runOnUiThread {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.sets.collect { sets ->
                adapter.updateList(sets)
            }
        }
    }

    companion object {
        private const val EXTRA_DAY_PLAN_IDS = "day_plan_ids"
        private const val EXTRA_EXERCISE_ID = "exercise_id"
        private const val EXTRA_EXERCISE_NAME = "exercise_name"

        fun createIntent(
            context: Context,
            dayPlanIds: List<Long>,
            exerciseId: Long,
            exerciseName: String
        ): Intent {
            return Intent(context, ExerciseSetPlanActivity::class.java).apply {
                putExtra(EXTRA_DAY_PLAN_IDS, dayPlanIds.toLongArray())
                putExtra(EXTRA_EXERCISE_ID, exerciseId)
                putExtra(EXTRA_EXERCISE_NAME, exerciseName)
            }
        }
    }
}