package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Workout.RoutineListItemUi
import com.TFG_JCF.fittrack.databinding.ItemRoutineBinding


class RoutineListAdapter(
    private val onActivateClick: (RoutineListItemUi) -> Unit,
    private val onRoutineClick: (RoutineListItemUi) -> Unit,
    private val onDeleteClick: (RoutineListItemUi) -> Unit
) : RecyclerView.Adapter<RoutineListViewHolder>() {

    private val items = mutableListOf<RoutineListItemUi>()

    fun submitList(newItems: List<RoutineListItemUi>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineListViewHolder {
        val binding = ItemRoutineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoutineListViewHolder(binding, onActivateClick, onRoutineClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: RoutineListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}