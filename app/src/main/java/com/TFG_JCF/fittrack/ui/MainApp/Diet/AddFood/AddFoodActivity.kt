package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity
import com.TFG_JCF.fittrack.databinding.ActivityAddFoodBinding
import com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.FoodMenu.FoodMenuActivity
import com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.adapter.AddFoodAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.emptyList


@AndroidEntryPoint
class AddFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFoodBinding
    private lateinit var adapter: AddFoodAdapter
    private lateinit var mealType :String
    private lateinit var selectedDate: String

    private val viewModel: AddFoodViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mealType = intent.getStringExtra("MEAL_TYPE").toString()
        selectedDate = intent.getStringExtra("SELECTED_DATE").orEmpty()
        initUI()


    }

    private fun initUI() {
        initSearchView()

        binding.tvMealType.text = mealType
        binding.goBack.setOnClickListener {
            onBackPressed()
        }
        initRecyclerView()
        suscribeViewModel()

        // primero cargo seedData y luego carga
        viewModel.prepareFoods()
    }


    private fun initRecyclerView() {
        adapter = AddFoodAdapter(emptyList()){
            selectedFood -> navigateToFoodMenu(selectedFood)
        }
        binding.rvFoods.layoutManager = LinearLayoutManager(this)
        binding.rvFoods.adapter = adapter
    }

    private fun navigateToFoodMenu(food: FoodEntity) {
        val intent = Intent(this, FoodMenuActivity::class.java).apply {
            putExtra("FOOD_ID", food.id)
            putExtra("MEAL_TYPE", mealType)
            putExtra("SELECTED_DATE", selectedDate)
        }
        startActivity(intent)
    }

    private fun suscribeViewModel() {
        viewModel.filteredFoods.observe(this) { foods ->
            adapter.updateList(foods)
        }
    }

    private fun initSearchView(){
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                searchByName(newText.orEmpty())
                return false
            }

        })
    }

    private fun searchByName(query : String){
        viewModel.filterFoods(query)
    }

}