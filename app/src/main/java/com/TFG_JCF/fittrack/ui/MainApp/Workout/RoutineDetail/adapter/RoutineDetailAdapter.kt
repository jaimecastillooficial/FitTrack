package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Workout.RoutineDetailItemUi
import com.TFG_JCF.fittrack.databinding.ItemRoutineBlockBinding

class RoutineDetailAdapter(
    private val onViewExercisesClick: (RoutineDetailItemUi) -> Unit,
    private val onEditDaysClick: (RoutineDetailItemUi) -> Unit
) : RecyclerView.Adapter<RoutineDetailViewHolder>() {

    private val items = mutableListOf<RoutineDetailItemUi>()

    fun submitList(newItems: List<RoutineDetailItemUi>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineDetailViewHolder {
        val binding = ItemRoutineBlockBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoutineDetailViewHolder(binding, onViewExercisesClick, onEditDaysClick)
    }

    override fun onBindViewHolder(holder: RoutineDetailViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}