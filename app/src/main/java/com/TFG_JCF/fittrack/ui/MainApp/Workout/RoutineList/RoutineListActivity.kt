package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineList

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.databinding.ActivityRoutineListBinding
import com.TFG_JCF.fittrack.databinding.DialogCreateRoutineBinding
import com.TFG_JCF.fittrack.databinding.DialogDeleteRoutineBinding
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.RoutineDetailActivity
import com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineList.adapter.RoutineListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoutineListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutineListBinding

    private val viewModel: RoutineListViewModel by viewModels()

    private lateinit var routinesAdapter: RoutineListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutineListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        initListeners()
        observeViewModel()

        viewModel.loadRoutines()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRoutines()
    }

    private fun setupRecycler() {
        routinesAdapter = RoutineListAdapter(
            onActivateClick = { routine ->
                viewModel.setActiveRoutine(routine.id)
                Toast.makeText(
                    this,
                    "Rutina activa: ${routine.name}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onRoutineClick = { routine ->
                val intent = Intent(this, RoutineDetailActivity::class.java)
                intent.putExtra("routineId", routine.id)
                startActivity(intent)
            },
            onDeleteClick = { routine ->
                showDeleteRoutineDialog(routine.id, routine.name)
            }

        )

        binding.rvRoutines.apply {
            layoutManager = LinearLayoutManager(this@RoutineListActivity)
            adapter = routinesAdapter
        }
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCreateRoutine.setOnClickListener {
            showCreateRoutineDialog()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.routines.collect { routines ->
                routinesAdapter.submitList(routines)
                binding.tvEmptyRoutines.isVisible = routines.isEmpty()
            }
        }
    }

    private fun showCreateRoutineDialog() {

        val binding = DialogCreateRoutineBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        dialog.show()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.btnCancelRoutineDialog.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnCreateRoutineDialog.setOnClickListener {
            val name = binding.etRoutineName.text?.toString()?.trim() ?: ""

            if (name.isBlank()) {
                binding.tilRoutineName.error = "Introduce un nombre válido"
            } else {
                binding.tilRoutineName.error = null
                viewModel.createRoutine(name)
                dialog.dismiss()
            }
        }
    }

    private fun showDeleteRoutineDialog(routineId: Long, routineName: String) {

        val binding = DialogDeleteRoutineBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        dialog.show()


        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        binding.tvDialogMessage.text =
            "Esta acción no se puede deshacer.\n\n¿Seguro que quieres eliminar \"$routineName\"?"

        binding.btnCancelDeleteDialog.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnConfirmDeleteDialog.setOnClickListener {
            viewModel.deleteRoutine(routineId)

            Toast.makeText(
                this,
                "Rutina eliminada",
                Toast.LENGTH_SHORT
            ).show()

            dialog.dismiss()
        }
    }
}