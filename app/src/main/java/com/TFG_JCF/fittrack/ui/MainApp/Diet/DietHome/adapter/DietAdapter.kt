package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Diet.MealListItem
import com.TFG_JCF.fittrack.databinding.ItemDietHeaderMealBinding
import com.TFG_JCF.fittrack.databinding.ItemDietMealBinding

class DietAdapter(
    private var meals: List<MealListItem> = emptyList(),
    private val onAddClick: (MealListItem.Header) -> Unit,
    private val onFoodClick: (MealListItem.FoodItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_FOOD = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_HEADER) {
            val binding = ItemDietHeaderMealBinding.inflate(inflater, parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemDietMealBinding.inflate(inflater, parent, false)
            FoodViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = meals[position]

        if (holder is HeaderViewHolder && item is MealListItem.Header) {
            holder.bind(item, onAddClick)
        }

        if (holder is FoodViewHolder && item is MealListItem.FoodItem) {
            holder.bind(item, onFoodClick)
        }
    }


    override fun getItemCount(): Int = meals.size


    override fun getItemViewType(position: Int): Int {
        return when (meals[position]) {
            is MealListItem.Header -> TYPE_HEADER
            is MealListItem.FoodItem -> TYPE_FOOD
        }
    }

    fun updateList(newMeals: List<MealListItem>) {
        meals = newMeals
        notifyDataSetChanged()
    }

}