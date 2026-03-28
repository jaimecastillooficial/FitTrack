package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity

class AddFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//TODO arreglar binding
    private val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)
    private val tvFoodCalories: TextView = itemView.findViewById(R.id.tvKcal)
    fun bind(food: FoodEntity, onFoodClick: (FoodEntity) -> Unit) {
        tvFoodName.text = food.name
        tvFoodCalories.text = "${food.kcalPer100g} kcal"

        itemView.setOnClickListener {
            onFoodClick(food)
        }
    }

}