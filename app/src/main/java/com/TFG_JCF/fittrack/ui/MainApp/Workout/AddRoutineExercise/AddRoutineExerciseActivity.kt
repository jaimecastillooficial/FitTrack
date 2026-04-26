package com.TFG_JCF.fittrack.ui.MainApp.Workout.AddRoutineExercise

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.MovementPattern
import com.TFG_JCF.fittrack.databinding.ActivityAddRoutineExerciseBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.AddRoutineExercise.adapter.AddRoutineExerciseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRoutineExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRoutineExerciseBinding
    private val viewModel: AddRoutineExerciseViewModel by viewModels()

    private lateinit var adapter: AddRoutineExerciseAdapter

    private var blockTitle: String = ""
    private var dayPlanIds: List<Long> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRoutineExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blockTitle = intent.getStringExtra(EXTRA_BLOCK_TITLE).orEmpty()
        dayPlanIds = intent.getLongArrayExtra(EXTRA_DAY_PLAN_IDS)?.toList() ?: emptyList()

        if (blockTitle.isBlank() || dayPlanIds.isEmpty()) {
            finish()
            return
        }

        initUI()
        viewModel.prepareExercises()
    }

    private fun initUI() {
        binding.tvBlockTitle.text = blockTitle

        binding.btnBack.setOnClickListener {
            finish()
        }

        initSearchView()
        initFilters()
        initRecycler()
        observeViewModel()
    }

    private fun initSearchView() {
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterByName(newText.orEmpty())
                return false
            }
        })
    }

    private fun initFilters() {
        binding.chipAll.setOnClickListener {
            viewModel.filterByPattern(null)
        }

        binding.chipPush.setOnClickListener {
            viewModel.filterByPattern(MovementPattern.PUSH)
        }

        binding.chipPull.setOnClickListener {
            viewModel.filterByPattern(MovementPattern.PULL)
        }

        binding.chipLegs.setOnClickListener {
            viewModel.filterByPattern(MovementPattern.LEGS)
        }

        binding.chipCore.setOnClickListener {
            viewModel.filterByPattern(MovementPattern.CORE)
        }
    }

    private fun initRecycler() {
        adapter = AddRoutineExerciseAdapter(
            exerciseList = emptyList(),
            onExerciseClick = { exercise ->
                addExercise(exercise)
            }
        )

        binding.rvExercises.layoutManager = LinearLayoutManager(this)
        binding.rvExercises.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.filteredExercises.observe(this) { exercises ->
            adapter.updateList(exercises)
        }
    }

    private fun addExercise(exercise: ExerciseEntity) {
        viewModel.addExerciseToBlock(
            dayPlanIds = dayPlanIds,
            exerciseId = exercise.id,
            onSuccess = {
                runOnUiThread {
                    Toast.makeText(this, "Ejercicio añadido", Toast.LENGTH_SHORT).show()
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

    companion object {
        private const val EXTRA_BLOCK_TITLE = "block_title"
        private const val EXTRA_DAY_PLAN_IDS = "day_plan_ids"

        fun createIntent(
            activity: AppCompatActivity,
            blockTitle: String,
            dayPlanIds: List<Long>
        ): Intent {
            return Intent(activity, AddRoutineExerciseActivity::class.java).apply {
                putExtra(EXTRA_BLOCK_TITLE, blockTitle)
                putExtra(EXTRA_DAY_PLAN_IDS, dayPlanIds.toLongArray())
            }
        }
    }
}