package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.model.Workout.RoutineExerciseItemUi

class RoutineExerciseAdapter(
    private var exerciseList: List<RoutineExerciseItemUi>,
    private val onDeleteClick: (RoutineExerciseItemUi) -> Unit,
    private val onItemClick: (RoutineExerciseItemUi) -> Unit
) : RecyclerView.Adapter<RoutineExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine_exercise, parent, false)

        return RoutineExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineExerciseViewHolder, position: Int) {
        holder.bind(exerciseList[position], onDeleteClick, onItemClick)
    }

    override fun getItemCount(): Int = exerciseList.size

    fun updateList(newList: List<RoutineExerciseItemUi>) {
        exerciseList = newList
        notifyDataSetChanged()
    }
}