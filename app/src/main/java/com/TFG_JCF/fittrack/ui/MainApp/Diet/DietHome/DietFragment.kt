package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.model.MealListItem
import com.TFG_JCF.fittrack.databinding.DialogEditMealItemBinding
import com.TFG_JCF.fittrack.databinding.FragmentDietBinding
import com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.AddFoodActivity
import com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome.adapter.DietAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

//class DietFragment : Fragment() {
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_diet, container, false)
//    }
//
//
//}

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
        observeViewModel()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecycler() {
        viewModel.loadDietForToday()

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
                if ( remaining < 0){
                    binding.tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.ft_error))
                }
                binding.tvResult.text = "$remaining"
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
        viewModel.loadDietForToday()
    }
    private fun navigateToAddFood(name: String) {
        val intent = Intent(this.requireContext(), AddFoodActivity::class.java)
        intent.putExtra("MEAL_TYPE", name)
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


        // GUARDAR CAMBIOS
        dialogBinding.btnSave.setOnClickListener {
            val newGrams = dialogBinding.etDialogGrams.text.toString().toFloatOrNull()

            if (newGrams == null || newGrams <= 0f) {
                Toast.makeText(requireContext(), "Introduce unos gramos válidos", Toast.LENGTH_SHORT).show()
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
}


