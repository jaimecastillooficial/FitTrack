package com.TFG_JCF.fittrack.ui.MainApp.Workout.WorkoutHistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Workout.WorkoutHistoryItemUi
import com.TFG_JCF.fittrack.databinding.ItemWorkoutHistoryBinding

class WorkoutHistoryAdapter : RecyclerView.Adapter<WorkoutHistoryViewHolder>() {

    private val items = mutableListOf<WorkoutHistoryItemUi>()

    fun submitList(newItems: List<WorkoutHistoryItemUi>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val binding = ItemWorkoutHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
