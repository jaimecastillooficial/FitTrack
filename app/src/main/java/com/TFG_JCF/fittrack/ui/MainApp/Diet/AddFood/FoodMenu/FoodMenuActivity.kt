package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.FoodMenu



import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType
import com.TFG_JCF.fittrack.databinding.ActivityFoodMenuBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FoodMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding
    private val viewModel: FoodMenuViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initData()
        initUI()
        observeState()
    }
//Preguntar
    private fun initData() {
        val foodId = intent.getLongExtra("FOOD_ID", 0L)
        val mealType = intent.getStringExtra("MEAL_TYPE") ?: "DESAYUNO"

        viewModel.loadFood(foodId, mealType)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        binding.goBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.saveFood(
                onSuccess = {
                    Toast.makeText(this, "Alimento guardado", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { msg ->
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.inputMealType.setOnClickListener {
            showMealTypeMenu()
        }

        binding.inputMealType.setOnLongClickListener {
            showMealTypeMenu()
            true
        }

        binding.InputPesoRacion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                viewModel.updateGrams(s?.toString().orEmpty())
            }
        })

        if (binding.InputPesoRacion.text.isNullOrBlank()) {
            binding.InputPesoRacion.setText("100")
        }
    }

    private fun showMealTypeMenu() {
        val popup = PopupMenu(this, binding.inputMealType)
        popup.menu.add(0, 1, 0, "Desayuno")
        popup.menu.add(0, 2, 1, "Comida")
        popup.menu.add(0, 3, 2, "Cena")
        popup.menu.add(0, 4, 3, "Snack")

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> viewModel.updateMealType(MealType.DESAYUNO)
                2 -> viewModel.updateMealType(MealType.COMIDA)
                3 -> viewModel.updateMealType(MealType.CENA)
                4 -> viewModel.updateMealType(MealType.SNACK)
            }
            true
        }

        popup.show()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.tvfoodName.text = state.foodName
                binding.inputMealType.text = mealTypeToText(state.mealType)
                updateChart(
                    calories = state.calories,
                    protein = state.protein,
                    carbs = state.carbs,
                    fat = state.fat
                )
            }
        }
    }

    private fun mealTypeToText(mealType: MealType): String {
        return when (mealType) {
            MealType.DESAYUNO -> "Desayuno"
            MealType.COMIDA -> "Comida"
            MealType.CENA -> "Cena"
            MealType.SNACK -> "Snack"
        }
    }

    private fun updateChart(
        calories: Int,
        protein: Float,
        carbs: Float,
        fat: Float
    ) {
        val entries = arrayListOf<PieEntry>()

        if (carbs > 0f) entries.add(PieEntry(carbs, "Carbohidratos"))
        if (protein > 0f) entries.add(PieEntry(protein, "Proteínas"))
        if (fat > 0f) entries.add(PieEntry(fat, "Grasas"))

        val dataSet = PieDataSet(entries, "")
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)

        binding.chartCalories.data = PieData(dataSet)
        binding.chartCalories.description.isEnabled = false
        binding.chartCalories.legend.isEnabled = false
        binding.chartCalories.centerText = "${calories} kcal"
        binding.chartCalories.setCenterTextSize(18f)
        binding.chartCalories.setEntryLabelTextSize(12f)
        binding.chartCalories.animateY(300)
        binding.chartCalories.invalidate()
    }
}