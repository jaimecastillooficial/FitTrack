package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome.adapter

import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.MealListItem
import com.TFG_JCF.fittrack.databinding.ItemDietMealBinding

class FoodViewHolder(
    private val binding: ItemDietMealBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MealListItem.FoodItem, onFoodClick: (MealListItem.FoodItem) -> Unit) {
        binding.tvFoodName.text = item.name
        binding.tvFoodGrams.text = "${item.grams} g"
        binding.tvKcal.text = "${item.calories} kcal"

        binding.root.setOnClickListener {
            onFoodClick(item)
        }
    }
}