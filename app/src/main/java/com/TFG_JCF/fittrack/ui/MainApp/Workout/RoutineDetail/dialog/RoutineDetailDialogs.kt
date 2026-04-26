package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.CheckBox
import com.TFG_JCF.fittrack.databinding.DialogAddBlockBinding
import com.TFG_JCF.fittrack.databinding.DialogDeleteBlockBinding
import com.TFG_JCF.fittrack.databinding.DialogEditDaysBinding
import com.TFG_JCF.fittrack.databinding.DialogRenameBlockBinding

class RoutineDetailDialogs(
    private val context: Context
) {

    fun showAddBlockDialog(
        onSave: (blockName: String, selectedDays: List<Int>) -> Unit
    ) {
        val dialog = Dialog(context)
        val binding = DialogAddBlockBinding.inflate(LayoutInflater.from(context))
        setupDialog(dialog, binding.root)
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnSave.setOnClickListener {
            val blockName = binding.etBlockName.text.toString()
            val selectedDays = getSelectedDays(
                cbMonday = binding.cbMonday,
                cbTuesday = binding.cbTuesday,
                cbWednesday = binding.cbWednesday,
                cbThursday = binding.cbThursday,
                cbFriday = binding.cbFriday,
                cbSaturday = binding.cbSaturday,
                cbSunday = binding.cbSunday
            )

            onSave(blockName, selectedDays)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showEditDaysDialog(
        blockName: String,
        currentDays: List<Int>,
        onSave: (selectedDays: List<Int>) -> Unit
    ) {
        val dialog = Dialog(context)
        val binding = DialogEditDaysBinding.inflate(LayoutInflater.from(context))
        setupDialog(dialog, binding.root)
        binding.tvDialogTitle.text = "Editar días de $blockName"

        setCheckedDays(
            days = currentDays,
            cbMonday = binding.cbMonday,
            cbTuesday = binding.cbTuesday,
            cbWednesday = binding.cbWednesday,
            cbThursday = binding.cbThursday,
            cbFriday = binding.cbFriday,
            cbSaturday = binding.cbSaturday,
            cbSunday = binding.cbSunday
        )
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnSave.setOnClickListener {
            val selectedDays = getSelectedDays(
                cbMonday = binding.cbMonday,
                cbTuesday = binding.cbTuesday,
                cbWednesday = binding.cbWednesday,
                cbThursday = binding.cbThursday,
                cbFriday = binding.cbFriday,
                cbSaturday = binding.cbSaturday,
                cbSunday = binding.cbSunday
            )

            onSave(selectedDays)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showRenameBlockDialog(
        currentName: String,
        onSave: (newName: String) -> Unit
    ) {
        val dialog = Dialog(context)
        val binding = DialogRenameBlockBinding.inflate(LayoutInflater.from(context))
        setupDialog(dialog, binding.root)
        binding.etBlockName.setText(currentName)
        binding.etBlockName.setSelection(binding.etBlockName.text.length)

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnSave.setOnClickListener {
            val newName = binding.etBlockName.text.toString()
            onSave(newName)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDeleteBlockDialog(
        blockName: String,
        onDelete: () -> Unit
    ) {
        val dialog = Dialog(context)
        val binding = DialogDeleteBlockBinding.inflate(LayoutInflater.from(context))
        setupDialog(dialog, binding.root)
        binding.tvDeleteMessage.text =
            "¿Quieres eliminar el bloque $blockName? Se eliminará de todos sus días asignados."

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            onDelete()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setupDialog(
        dialog: Dialog,
        rootView: android.view.View
    ) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(rootView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setCheckedDays(
        days: List<Int>,
        cbMonday: CheckBox, cbTuesday: CheckBox,
        cbWednesday: CheckBox, cbThursday: CheckBox,
        cbFriday: CheckBox, cbSaturday: CheckBox,
        cbSunday: CheckBox
    ) {
        cbMonday.isChecked = days.contains(1)
        cbTuesday.isChecked = days.contains(2)
        cbWednesday.isChecked = days.contains(3)
        cbThursday.isChecked = days.contains(4)
        cbFriday.isChecked = days.contains(5)
        cbSaturday.isChecked = days.contains(6)
        cbSunday.isChecked = days.contains(7)
    }

    private fun getSelectedDays(
        cbMonday: CheckBox, cbTuesday: CheckBox,
        cbWednesday: CheckBox, cbThursday: CheckBox,
        cbFriday: CheckBox, cbSaturday: CheckBox,
        cbSunday: CheckBox
    ): List<Int> {
        val selectedDays = mutableListOf<Int>()

        if (cbMonday.isChecked) selectedDays.add(1)
        if (cbTuesday.isChecked) selectedDays.add(2)
        if (cbWednesday.isChecked) selectedDays.add(3)
        if (cbThursday.isChecked) selectedDays.add(4)
        if (cbFriday.isChecked) selectedDays.add(5)
        if (cbSaturday.isChecked) selectedDays.add(6)
        if (cbSunday.isChecked) selectedDays.add(7)

        return selectedDays
    }
}