package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineList.adapter



import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.model.Workout.RoutineListItemUi
import com.TFG_JCF.fittrack.databinding.ItemRoutineBinding

class RoutineListViewHolder(
    private val binding: ItemRoutineBinding,
    private val onActivateClick: (RoutineListItemUi) -> Unit,
    private val onRoutineClick: (RoutineListItemUi) -> Unit,
    private val onDeleteClick: (RoutineListItemUi) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RoutineListItemUi) {
        binding.tvRoutineName.text = item.name

        if (item.isActive) {
            setUpActiveRoutine()
        } else {
            setUpInActiveRoutine()
        }

        binding.root.setOnClickListener {
            onRoutineClick(item)
        }

        binding.btnActivateRoutine.setOnClickListener {
            onActivateClick(item)
        }
        binding.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    fun setUpActiveRoutine(){
        binding.tvRoutineStatus.text = "Activa"
        binding.btnActivateRoutine.text = "Activa"
        binding.btnActivateRoutine.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.ft_primary_disabled)
        binding.tvRoutineStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.ft_success))
        binding.tvRoutineStatus.setTypeface(binding.tvRoutineStatus.typeface, Typeface.BOLD)
        binding.btnActivateRoutine.isEnabled = false
    }

    fun setUpInActiveRoutine(){
        binding.tvRoutineStatus.text = "No activa"
        binding.tvRoutineStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.ft_text_secondary))
        binding.btnActivateRoutine.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.fittrack)
        binding.tvRoutineStatus.setTypeface(binding.tvRoutineStatus.typeface, Typeface.NORMAL)
        binding.btnActivateRoutine.text = "Activar"
        binding.btnActivateRoutine.isEnabled = true
    }

}