package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity

class AddFoodAdapter(
    private var foodList: List<FoodEntity>,
    private val onFoodClick: (FoodEntity) -> Unit
) : RecyclerView.Adapter<AddFoodViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddFoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diet_meal, parent, false)
        return AddFoodViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AddFoodViewHolder,
        position: Int
    ) {
        val food = foodList[position]
        holder.bind(food, onFoodClick)
    }

    override fun getItemCount(): Int = foodList.size


    fun updateList(newList: List<FoodEntity>) {
        foodList = newList
        notifyDataSetChanged()
    }
}