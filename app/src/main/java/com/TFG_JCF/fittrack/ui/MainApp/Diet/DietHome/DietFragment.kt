package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.model.Diet.MealListItem
import com.TFG_JCF.fittrack.databinding.DialogEditMealItemBinding
import com.TFG_JCF.fittrack.databinding.DialogSelectDateBinding
import com.TFG_JCF.fittrack.databinding.FragmentDietBinding
import com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.AddFoodActivity
import com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome.adapter.DietAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate


@AndroidEntryPoint
class DietFragment : Fragment(R.layout.fragment_diet) {


    private var _binding: FragmentDietBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DietViewModel by viewModels()

    private lateinit var adapter: DietAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDietBinding.bind(view)

        initRecycler()
        initDateListeners()
        observeViewModel()



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecycler() {
        viewModel.loadDietForSelectedDate()
        //Le paso las 2 lambdas
        adapter = DietAdapter(
            onAddClick = { header ->
                navigateToAddFood(header.mealType.toString())
            },
            onFoodClick = { foodItem ->
                showFoodOptionsDialog(foodItem)
            }
        )

        binding.rvMeals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMeals.adapter = adapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDateListeners() {
        binding.btnPreviousDay.setOnClickListener {
            viewModel.goToPreviousDay()
        }

        binding.btnNextDay.setOnClickListener {
            viewModel.goToNextDay()
        }

        binding.tvSelectedDate.setOnClickListener {
            showDatePickerDialog()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        // Actualizar lista de alimentos ingeridos
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { list ->
                adapter.updateList(list)
            }
        }
        // Calorias objetivo
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesGoal.collect { goal ->
                binding.tvCaloriesGoal.text = "$goal"

            }
        }
        // Calorias consumidas
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesConsumed.collect { consumed ->
                binding.tvMealCalories.text = "$consumed"

            }
        }
        // Calorias restantes
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesRemaining.collect { remaining ->
                if (remaining < 0) {
                    binding.tvResult.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.ft_error
                        )
                    )
                }
                binding.tvResult.text = "$remaining"
            }
        }
        //Fecha seleccionada
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedDate.collect { date ->
                binding.tvSelectedDate.text = date.toString()

                val isToday = date == LocalDate.now()
                binding.btnNextDay.isEnabled = !isToday
                binding.btnNextDay.alpha = if (isToday) 0.4f else 1f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.loadDietForSelectedDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToAddFood(name: String) {
        val intent = Intent(this.requireContext(), AddFoodActivity::class.java)
        intent.putExtra("MEAL_TYPE", name)
        intent.putExtra("SELECTED_DATE", viewModel.selectedDate.value.toString())
        startActivity(intent)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showFoodOptionsDialog(item: MealListItem.FoodItem) {
        val dialogBinding = DialogEditMealItemBinding.inflate(layoutInflater)

        dialogBinding.tvDialogFoodName.text = item.name
        dialogBinding.etDialogGrams.setText(item.grams.toString())

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // GUARDAR CAMBIOS
        dialogBinding.btnSave.setOnClickListener {
            val newGrams = dialogBinding.etDialogGrams.text.toString().toFloatOrNull()

            if (newGrams == null || newGrams <= 0f) {
                Toast.makeText(
                    requireContext(),
                    "Introduce unos gramos válidos",
                    Toast.LENGTH_SHORT
                ).show()
                //Indico que pare aqui y salga del OnClick pero al no haber un .dismiss el dialog sigue abierto
                return@setOnClickListener
            }

            viewModel.updateMealItemGrams(item.mealItemId, newGrams) {
                Toast.makeText(requireContext(), "Gramos actualizados", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        // ELIMINAR
        dialogBinding.btnDelete.setOnClickListener {
            viewModel.deleteMealItem(item.mealItemId) {
                Toast.makeText(requireContext(), "Alimento eliminado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val dialogBinding = DialogSelectDateBinding.inflate(layoutInflater)
        val currentDate = viewModel.selectedDate.value

        dialogBinding.datePicker.init(
            currentDate.year,
            currentDate.monthValue - 1,
            currentDate.dayOfMonth,
            null
        )

        dialogBinding.datePicker.maxDate = System.currentTimeMillis()

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnAccept.setOnClickListener {
            val year = dialogBinding.datePicker.year
            val month = dialogBinding.datePicker.month
            val day = dialogBinding.datePicker.dayOfMonth

            val selectedDate = LocalDate.of(year, month + 1, day)

            if (selectedDate.isAfter(LocalDate.now())) {
                Toast.makeText(
                    requireContext(),
                    "No puedes registrar comidas en fechas futuras",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.setSelectedDate(selectedDate)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}


