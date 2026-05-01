package com.TFG_JCF.fittrack.ui.MainApp.Workout.ExerciseSetPlan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Workout.SetPlanInput
import com.TFG_JCF.fittrack.databinding.ItemExerciseSetPlanBinding

class ExerciseSetPlanAdapter(
    private var items: List<SetPlanInput>,
    private val onItemChanged: (Int, SetPlanInput) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ExerciseSetPlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSetPlanViewHolder {
        val binding = ItemExerciseSetPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ExerciseSetPlanViewHolder(
            binding = binding,
            onItemChanged = onItemChanged,
            onDeleteClick = onDeleteClick
        )
    }


    override fun onBindViewHolder(holder: ExerciseSetPlanViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<SetPlanInput>) {
        items = newItems
        notifyDataSetChanged()
    }
}