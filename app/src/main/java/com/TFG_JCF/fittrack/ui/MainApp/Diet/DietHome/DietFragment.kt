package com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentDietBinding
import com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.AddFoodActivity
import com.TFG_JCF.fittrack.ui.MainApp.Diet.DietHome.adapter.DietAdapter
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

class DietFragment : Fragment(R.layout.fragment_diet) {


    private var _binding: FragmentDietBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DietViewModel by viewModels()

    private lateinit var adapter: DietAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDietBinding.bind(view)

        initRecycler()
        observeViewModel()

        viewModel.loadFakeData()
    }

    private fun initRecycler() {
        adapter = DietAdapter{ header ->
                navigateToAddFood(header.mealType.toString())
            }


        binding.rvMeals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMeals.adapter = adapter
    }

    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { list ->
                adapter.updateList(list)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesGoal.collect { goal ->
                binding.tvCaloriesGoal.text = "$goal"

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesConsumed.collect { consumed ->
                binding.tvMealCalories.text = "$consumed"

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.caloriesRemaining.collect { remaining ->
                binding.tvResult.text = "$remaining"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun navigateToAddFood(name : String) {
        val intent = Intent(this.requireContext(), AddFoodActivity::class.java)
        intent.putExtra("MEAL_TYPE", name)
        startActivity(intent)

    }
}


