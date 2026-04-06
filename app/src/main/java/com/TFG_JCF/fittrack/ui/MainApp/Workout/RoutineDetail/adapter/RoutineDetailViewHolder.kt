package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Workout.RoutineDetailItemUi
import com.TFG_JCF.fittrack.databinding.ItemRoutineBlockBinding

class RoutineDetailViewHolder(
    private val binding: ItemRoutineBlockBinding,
    private val onViewExercisesClick: (RoutineDetailItemUi) -> Unit,
    private val onEditDaysClick: (RoutineDetailItemUi) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RoutineDetailItemUi) {
        binding.tvBlockName.text = item.title
        binding.tvAssignedDays.text = item.selectedDays
            .sorted()
            .joinToString(", ") { dayNumberToText(it) }

        binding.btnViewExercises.setOnClickListener {
            onViewExercisesClick(item)
        }

        binding.btnEditDays.setOnClickListener {
            onEditDaysClick(item)
        }
    }

    private fun dayNumberToText(day: Int): String {
        return when (day) {
            1 -> "Lunes"
            2 -> "Martes"
            3 -> "Miércoles"
            4 -> "Jueves"
            5 -> "Viernes"
            6 -> "Sábado"
            7 -> "Domingo"
            else -> "-"
        }
    }
}