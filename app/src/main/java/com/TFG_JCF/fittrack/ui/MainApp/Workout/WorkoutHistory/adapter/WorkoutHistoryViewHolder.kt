package com.TFG_JCF.fittrack.ui.MainApp.Workout.WorkoutHistory.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Workout.WorkoutHistoryItemUi
import com.TFG_JCF.fittrack.databinding.ItemWorkoutHistoryBinding

class WorkoutHistoryViewHolder(
    private val binding: ItemWorkoutHistoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var expanded = false

    fun bind(item: WorkoutHistoryItemUi) {
        expanded = false

        binding.tvWorkoutDate.text = item.date
        binding.tvWorkoutDayName.text = item.dayName
        binding.tvWorkoutSummary.text = "${item.exerciseCount} ejercicios · ${item.setCount} series"
        binding.tvWorkoutDetail.text = item.detailText
        binding.tvWorkoutDetail.isVisible = false
        binding.btnToggleDetail.text = "Ver detalle"

        binding.btnToggleDetail.setOnClickListener {
            expanded = !expanded
            binding.tvWorkoutDetail.isVisible = expanded
            binding.btnToggleDetail.text = if (expanded) "Ocultar detalle" else "Ver detalle"
        }
    }
}
