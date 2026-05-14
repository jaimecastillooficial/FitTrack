package com.TFG_JCF.fittrack.ui.MainApp.Progress

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.TFG_JCF.fittrack.databinding.DialogAddWeightBinding
import com.TFG_JCF.fittrack.databinding.FragmentProgressBinding
import com.TFG_JCF.fittrack.ui.MainApp.Progress.adapter.WeightEntryAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProgressViewModel by viewModels()
    private lateinit var adapter: WeightEntryAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProgressBinding.bind(view)

        initRecycler()
        initListeners()
        observeViewModel()
    }

    private fun initRecycler() {
        adapter = WeightEntryAdapter(
            onDeleteClick = { entry ->
                viewModel.deleteWeightEntry(entry)
                Toast.makeText(requireContext(), "Registro eliminado", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvWeightEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWeightEntries.adapter = adapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        binding.btnAddWeight.setOnClickListener {
            showAddWeightDialog()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weightEntries.collect { entries ->
                adapter.updateList(entries.reversed())
                updateSummary(entries)
                updateChart(entries)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userProfile.collect { profile ->
                binding.tvTargetWeight.text = profile?.targetWeight?.let {
                    "${it} kg"
                } ?: "Sin objetivo"
            }
        }
    }

    private fun updateSummary(entries: List<WeightEntryEntity>) {
        if (entries.isEmpty()) {
            binding.tvCurrentWeight.text = "-- kg"
            binding.tvTotalChange.text = "-- kg"
            binding.tvEmptyHistory.visibility = View.VISIBLE
            return
        }

        binding.tvEmptyHistory.visibility = View.GONE

        val firstWeight = entries.first().weightKg
        val lastWeight = entries.last().weightKg
        val change = lastWeight - firstWeight

        binding.tvCurrentWeight.text = "${lastWeight} kg"
        binding.tvTotalChange.text = String.format("%.1f kg", change)

        val color = if (change <= 0f) {
            R.color.ft_success
        } else {
            R.color.ft_warning
        }

        binding.tvTotalChange.setTextColor(
            ContextCompat.getColor(requireContext(), color)
        )
    }

    private fun updateChart(entries: List<WeightEntryEntity>) {
        if (entries.isEmpty()) {
            binding.lineChartWeight.clear()
            binding.lineChartWeight.invalidate()
            return
        }

        val chartEntries = entries.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.weightKg)
        }

        val dataSet = LineDataSet(chartEntries, "Peso")

        dataSet.lineWidth = 2.5f
        dataSet.circleRadius = 4f
        dataSet.valueTextSize = 10f
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.ft_primary)
        dataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.ft_primary))

        binding.lineChartWeight.data = LineData(dataSet)

        binding.lineChartWeight.description.isEnabled = false
        binding.lineChartWeight.legend.isEnabled = false
        binding.lineChartWeight.axisRight.isEnabled = false

        binding.lineChartWeight.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChartWeight.xAxis.granularity = 1f

        binding.lineChartWeight.animateX(300)
        binding.lineChartWeight.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAddWeightDialog() {
        val dialogBinding = DialogAddWeightBinding.inflate(layoutInflater)
        var selectedDate = LocalDate.now()

        fun updateDateButtonText() {
            dialogBinding.btnSelectDate.text = selectedDate.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
        }

        updateDateButtonText()

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.btnSelectDate.setOnClickListener {
            val dateDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    updateDateButtonText()
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            )

            // Evita registrar peso en fechas futuras.
            dateDialog.datePicker.maxDate = System.currentTimeMillis()
            dateDialog.show()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val weight = dialogBinding.etWeight.text
                .toString()
                .replace(',', '.')
                .toFloatOrNull()

            if (weight == null || weight <= 0f) {
                Toast.makeText(requireContext(), "Introduce un peso válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addWeight(weight, selectedDate)

            Toast.makeText(requireContext(), "Peso registrado", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProgressData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}