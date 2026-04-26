package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineExercise.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.Workout.toDisplayName
import com.TFG_JCF.fittrack.data.model.Workout.RoutineExerciseItemUi
import com.google.android.material.button.MaterialButton

class RoutineExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
    private val tvExerciseInfo: TextView = itemView.findViewById(R.id.tvExerciseInfo)
    private val btnDeleteExercise: MaterialButton = itemView.findViewById(R.id.btnDeleteExercise)

    fun bind(
        item: RoutineExerciseItemUi,
        onDeleteClick: (RoutineExerciseItemUi) -> Unit
    ) {
        tvExerciseName.text = item.name

        val patternText = item.movementPattern?.toDisplayName() ?: "Sin patrón"
        val muscleText = item.muscleGroup ?: "Sin grupo muscular"

        tvExerciseInfo.text = "$muscleText · $patternText"

        btnDeleteExercise.setOnClickListener {
            onDeleteClick(item)
        }
    }
}