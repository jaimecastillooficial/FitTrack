package com.TFG_JCF.fittrack.ui.MainApp.Workout.AddRoutineExercise.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.toDisplayName

class AddRoutineExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
    private val tvExerciseInfo: TextView = itemView.findViewById(R.id.tvExerciseInfo)

    fun bind(
        exercise: ExerciseEntity,
        onExerciseClick: (ExerciseEntity) -> Unit
    ) {
        tvExerciseName.text = exercise.name

        val muscleText = exercise.muscleGroup ?: "Sin grupo muscular"
        val patternText = exercise.movementPattern?.toDisplayName() ?: "Sin patrón"

        tvExerciseInfo.text = "$muscleText · $patternText"

        itemView.setOnClickListener {
            onExerciseClick(exercise)
        }
    }
}