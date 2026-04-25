package com.TFG_JCF.fittrack.ui.MainApp.Workout.AddRoutineExercise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity

class AddRoutineExerciseAdapter(
    private var exerciseList: List<ExerciseEntity>,
    private val onExerciseClick: (ExerciseEntity) -> Unit
) : RecyclerView.Adapter<AddRoutineExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRoutineExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_routine_exercise, parent, false)

        return AddRoutineExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddRoutineExerciseViewHolder, position: Int) {
        holder.bind(exerciseList[position], onExerciseClick)
    }

    override fun getItemCount(): Int = exerciseList.size

    fun updateList(newList: List<ExerciseEntity>) {
        exerciseList = newList
        notifyDataSetChanged()
    }
}