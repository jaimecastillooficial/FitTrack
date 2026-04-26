package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.model.Workout.RoutineDetailItemUi
import com.TFG_JCF.fittrack.databinding.ActivityRoutineDetailBinding

import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.adapter.RoutineDetailAdapter
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.dialog.RoutineDetailDialogs
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise.RoutineExerciseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoutineDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutineDetailBinding
    private val viewModel: RoutineDetailViewModel by viewModels()
    private var routineId: Long = -1L
    private lateinit var detailAdapter: RoutineDetailAdapter

    private lateinit var dialogs: RoutineDetailDialogs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogs = RoutineDetailDialogs(this)
        routineId = intent.getLongExtra(EXTRA_ROUTINE_ID, -1L)

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
            onBlockClick = { item ->
                startActivity(
                    RoutineExerciseActivity.createIntent(
                        activity = this,
                        blockTitle = item.title,
                        dayPlanIds = item.sourceDayPlanIds
                    )
                )
            },

            onEditDaysClick = { item ->
                showEditDaysDialog(item)
            },

            onEditNameClick = { item ->
                showRenameBlockDialog(item)
            },

            onDeleteBlockClick = { item ->
                showDeleteBlockDialog(item)
            }
        )

        binding.rvRoutineBlocks.apply {
            layoutManager = LinearLayoutManager(this@RoutineDetailActivity)
            adapter = detailAdapter
        }

        binding.btnAddBlock.setOnClickListener {
            showAddBlockDialog()
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

    private fun showAddBlockDialog() {
        dialogs.showAddBlockDialog { blockName, selectedDays ->
            viewModel.addBlockToRoutine(
                routineWeekId = routineId,
                blockName = blockName,
                selectedDays = selectedDays,
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onSuccess = {
                    Toast.makeText(this, "Bloque añadido", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun showEditDaysDialog(item: RoutineDetailItemUi) {
        dialogs.showEditDaysDialog(
            blockName = item.title,
            currentDays = item.selectedDays,
            onSave = { selectedDays ->
                viewModel.updateBlockDays(
                    routineWeekId = routineId,
                    blockTitle = item.title,
                    newSelectedDays = selectedDays,
                    onError = { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = {
                        Toast.makeText(this, "Días actualizados", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }

    private fun showRenameBlockDialog(item: RoutineDetailItemUi) {
        dialogs.showRenameBlockDialog(
            currentName = item.title
        ) { newName ->
            viewModel.renameBlock(
                routineWeekId = routineId,
                currentBlockTitle = item.title,
                newBlockName = newName,
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onSuccess = {
                    Toast.makeText(this, "Bloque actualizado", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun showDeleteBlockDialog(item: RoutineDetailItemUi) {
        dialogs.showDeleteBlockDialog(
            blockName = item.title
        ) {
            viewModel.deleteBlock(
                routineWeekId = routineId,
                blockTitle = item.title,
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onSuccess = {
                    Toast.makeText(this, "Bloque eliminado", Toast.LENGTH_SHORT).show()
                }
            )
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

