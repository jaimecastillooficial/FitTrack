package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.data.model.Workout.RoutineExerciseItemUi
import com.TFG_JCF.fittrack.databinding.ActivityRoutineExerciseBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise.adapter.RoutineExerciseAdapter
import com.TFG_JCF.fittrack.ui.MainApp.Workout.AddRoutineExercise.AddRoutineExerciseActivity
import com.TFG_JCF.fittrack.ui.MainApp.Workout.ExerciseSetPlan.ExerciseSetPlanActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoutineExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutineExerciseBinding
    private val viewModel: RoutineExerciseViewModel by viewModels()

    private lateinit var adapter: RoutineExerciseAdapter

    private var blockTitle: String = ""
    private var dayPlanIds: List<Long> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blockTitle = intent.getStringExtra(EXTRA_BLOCK_TITLE).orEmpty()
        dayPlanIds = intent.getLongArrayExtra(EXTRA_DAY_PLAN_IDS)?.toList() ?: emptyList()

        if (blockTitle.isBlank() || dayPlanIds.isEmpty()) {
            finish()
            return
        }

        initRecycler()
        initListeners()
        observeViewModel()

        viewModel.loadBlockExercises(blockTitle, dayPlanIds)
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadCurrentBlock()
    }

    private fun initRecycler() {
        adapter = RoutineExerciseAdapter(
            exerciseList = emptyList(),
            onDeleteClick = { item ->
                showDeleteDialog(item)
            },
            onItemClick = { item ->
                startActivity(
                    ExerciseSetPlanActivity.createIntent(
                        context = this,
                        dayPlanIds = dayPlanIds,
                        exerciseId = item.exerciseId,
                        exerciseName = item.name
                    )
                )
            }
        )

        binding.rvExercises.layoutManager = LinearLayoutManager(this)
        binding.rvExercises.adapter = adapter
    }


    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddExercise.setOnClickListener {
            startActivity(
                AddRoutineExerciseActivity.createIntent(
                    activity = this,
                    blockTitle = blockTitle,
                    dayPlanIds = dayPlanIds
                )
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.blockTitle.collect { title ->
                binding.tvBlockTitle.text = title
            }
        }

        lifecycleScope.launch {
            viewModel.items.collect { items ->
                adapter.updateList(items)
                binding.tvEmptyExercises.isVisible = items.isEmpty()
            }
        }

    }

    private fun showDeleteDialog(item: RoutineExerciseItemUi) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar ejercicio")
            .setMessage("¿Quieres eliminar ${item.name} de este bloque?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.removeExerciseFromBlock(
                    exerciseId = item.exerciseId,
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(this, "Ejercicio eliminado", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onError = { message ->
                        runOnUiThread {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            .show()
    }


    companion object {
        private const val EXTRA_BLOCK_TITLE = "block_title"
        private const val EXTRA_DAY_PLAN_IDS = "day_plan_ids"

        fun createIntent(
            activity: AppCompatActivity,
            blockTitle: String,
            dayPlanIds: List<Long>
        ): Intent {
            return Intent(activity, RoutineExerciseActivity::class.java).apply {
                putExtra(EXTRA_BLOCK_TITLE, blockTitle)
                putExtra(EXTRA_DAY_PLAN_IDS, dayPlanIds.toLongArray())
            }
        }
    }
}