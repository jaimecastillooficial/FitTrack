package com.TFG_JCF.fittrack.ui.MainApp.Workout.ExerciseSetPlan.adapter

import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.data.model.Workout.SetPlanInput
import com.TFG_JCF.fittrack.databinding.ItemExerciseSetPlanBinding

class ExerciseSetPlanViewHolder(
    private val binding: ItemExerciseSetPlanBinding,
    private val onItemChanged: (Int, SetPlanInput) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var weightWatcher: TextWatcher? = null
    private var repsWatcher: TextWatcher? = null
    private var rirWatcher: TextWatcher? = null

    // Se quitan los TextWatcher anteriores porque el RecyclerView reutiliza las vistas
    // si no se eliminan antes de volver a hacer bind, se pueden acumular varios listeners
    // en el mismo EditText y provocar actualizaciones duplicadas o incorrectas.
    fun bind(item: SetPlanInput, position: Int) {
        binding.tvSetNumber.text = "Serie ${position + 1}"

        weightWatcher?.let { binding.etWeight.removeTextChangedListener(it) }
        repsWatcher?.let { binding.etReps.removeTextChangedListener(it) }
        rirWatcher?.let { binding.etRir.removeTextChangedListener(it) }

        binding.etWeight.setText(item.weightText)
        binding.etReps.setText(item.repsText)
        binding.etRir.setText(item.rirText)

        weightWatcher = simpleWatcher {
            notifyCurrentTextsChanged()
        }

        repsWatcher = simpleWatcher {
            notifyCurrentTextsChanged()
        }

        rirWatcher = simpleWatcher {
            notifyCurrentTextsChanged()
        }

        binding.etWeight.addTextChangedListener(weightWatcher)
        binding.etReps.addTextChangedListener(repsWatcher)
        binding.etRir.addTextChangedListener(rirWatcher)

        binding.btnDeleteSet.setOnClickListener {
            val position = bindingAdapterPosition

            if (position != RecyclerView.NO_POSITION) {
                onDeleteClick(position)
            }
        }
    }

    private fun notifyCurrentTextsChanged() {
        val position = bindingAdapterPosition

        if (position == RecyclerView.NO_POSITION) return

        onItemChanged(
            position,
            SetPlanInput(
                weightText = binding.etWeight.text?.toString().orEmpty(),
                repsText = binding.etReps.text?.toString().orEmpty(),
                rirText = binding.etRir.text?.toString().orEmpty()
            )
        )
    }
    //Implementa 3 metodos los 2 primeros no hacen nada en esta app
    private fun simpleWatcher(afterChanged: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) = Unit

            override fun afterTextChanged(s: Editable?) {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    afterChanged()
                }
            }
        }
    }
}