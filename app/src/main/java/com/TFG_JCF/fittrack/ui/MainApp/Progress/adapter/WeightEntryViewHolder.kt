package com.TFG_JCF.fittrack.ui.MainApp.Progress.adapter

import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.TFG_JCF.fittrack.databinding.ItemWeightEntryBinding

class WeightEntryViewHolder(
    private val binding: ItemWeightEntryBinding,
    private val onDeleteClick: (WeightEntryEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entry: WeightEntryEntity) {
        binding.tvWeight.text = "${entry.weightKg} kg"
        binding.tvDate.text = entry.date

        binding.btnDeleteWeight.setOnClickListener {
            onDeleteClick(entry)
        }
    }
}