package com.TFG_JCF.fittrack.ui.MainApp.Progress.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.TFG_JCF.fittrack.databinding.ItemWeightEntryBinding

class WeightEntryAdapter(
    private val onDeleteClick: (WeightEntryEntity) -> Unit
) : RecyclerView.Adapter<WeightEntryViewHolder>() {

    private var entries: List<WeightEntryEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightEntryViewHolder {
        val binding = ItemWeightEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeightEntryViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: WeightEntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    fun updateList(newEntries: List<WeightEntryEntity>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}