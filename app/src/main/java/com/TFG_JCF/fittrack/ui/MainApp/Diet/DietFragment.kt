package com.TFG_JCF.fittrack.ui.MainApp.Diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.FragmentDietBinding
import com.TFG_JCF.fittrack.ui.MainApp.Diet.adapter.DietAdapter
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
        adapter = DietAdapter(
            onAddClick = { header ->
                Toast.makeText(
                    requireContext(),
                    "Añadir comida a ${header.mealType}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

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
                binding.tvResultText.text = "$remaining"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}