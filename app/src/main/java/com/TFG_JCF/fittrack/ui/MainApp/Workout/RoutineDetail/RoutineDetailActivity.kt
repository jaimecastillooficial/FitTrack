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
import com.TFG_JCF.fittrack.databinding.DialogAddRoutineBlockBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.adapter.RoutineDetailAdapter
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise.RoutineExerciseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoutineDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutineDetailBinding
    private val viewModel: RoutineDetailViewModel by viewModels()
    private var routineId: Long = -1L
    private lateinit var detailAdapter: RoutineDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val dialogBinding = DialogAddRoutineBlockBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar", null)
            .create()

        dialog.setOnShowListener {

            val btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            btnSave.setOnClickListener {

                val blockName = dialogBinding.etBlockName.text
                    ?.toString()
                    ?.trim()
                    .orEmpty()

                val selectedDays = mutableListOf<Int>()

                if (dialogBinding.cbMonday.isChecked) selectedDays.add(1)
                if (dialogBinding.cbTuesday.isChecked) selectedDays.add(2)
                if (dialogBinding.cbWednesday.isChecked) selectedDays.add(3)
                if (dialogBinding.cbThursday.isChecked) selectedDays.add(4)
                if (dialogBinding.cbFriday.isChecked) selectedDays.add(5)
                if (dialogBinding.cbSaturday.isChecked) selectedDays.add(6)
                if (dialogBinding.cbSunday.isChecked) selectedDays.add(7)

                // limpiar error anterior
                dialogBinding.tilBlockName.error = null

                viewModel.addBlockToRoutine(
                    routineWeekId = routineId,
                    blockName = blockName,
                    selectedDays = selectedDays,

                    onError = { message ->
                        runOnUiThread {
                            when {
                                message.equals(
                                    "Introduce un nombre para el bloque",
                                    true
                                ) || message.equals("Ya existe un bloque con ese nombre", true) -> {
                                    dialogBinding.tilBlockName.error = message
                                }

                                else -> {
                                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },

                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(this, "Bloque añadido", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                )
            }
        }

        dialog.show()
    }

    private fun showEditDaysDialog(item: RoutineDetailItemUi) {
        val dayLabels = arrayOf(
            "Lunes",
            "Martes",
            "Miércoles",
            "Jueves",
            "Viernes",
            "Sábado",
            "Domingo"
        )

        val checkedItems = BooleanArray(7) { index ->
            val dayNumber = index + 1
            item.selectedDays.contains(dayNumber)
        }

        AlertDialog.Builder(this)
            .setTitle("Selecciona los días para ${item.title}")
            .setMultiChoiceItems(dayLabels, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar") { _, _ ->
                val selectedDays = mutableListOf<Int>()

                checkedItems.forEachIndexed { index, checked ->
                    if (checked) {
                        selectedDays.add(index + 1)
                    }
                }

                viewModel.updateBlockDays(
                    routineWeekId = routineId,
                    blockTitle = item.title,
                    newSelectedDays = selectedDays,
                    onError = { message ->
                        runOnUiThread {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    },
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(this, "Días actualizados", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            .show()
    }

    private fun showRenameBlockDialog(item: RoutineDetailItemUi) {
        val input = EditText(this).apply {
            setText(item.title)
            setSelection(text.length)
            hint = "Nombre del bloque"
        }

        AlertDialog.Builder(this)
            .setTitle("Editar bloque")
            .setView(input)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = input.text.toString()

                viewModel.renameBlock(
                    routineWeekId = routineId,
                    currentBlockTitle = item.title,
                    newBlockName = newName,
                    onError = { message ->
                        runOnUiThread {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    },
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(this, "Bloque actualizado", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            .show()
    }

    private fun showDeleteBlockDialog(item: RoutineDetailItemUi) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar bloque")
            .setMessage("¿Quieres eliminar el bloque ${item.title}? Se eliminará de todos sus días asignados.")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.deleteBlock(
                    routineWeekId = routineId,
                    blockTitle = item.title,
                    onError = { message ->
                        runOnUiThread {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    },
                    onSuccess = {
                        runOnUiThread {
                            Toast.makeText(this, "Bloque eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            .show()
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

