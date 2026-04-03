package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome.adapter

import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType
import com.TFG_JCF.fittrack.data.model.Diet.MealListItem
import com.TFG_JCF.fittrack.databinding.ItemDietHeaderMealBinding


class HeaderViewHolder ( private val binding: ItemDietHeaderMealBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: MealListItem.Header,
        onAddClick: (MealListItem.Header) -> Unit
    ) {
        binding.tvHeader.text = getMealTitle(item.mealType)

        binding.btnAddFood.setOnClickListener {
           onAddClick(item)
        }
    }

    private fun getMealTitle(mealType: MealType): String {
        return when (mealType) {
            MealType.DESAYUNO -> "Desayuno"
            MealType.COMIDA -> "Comida"
            MealType.CENA -> "Cena"
            MealType.SNACK -> "Snack"
        }
    }
}