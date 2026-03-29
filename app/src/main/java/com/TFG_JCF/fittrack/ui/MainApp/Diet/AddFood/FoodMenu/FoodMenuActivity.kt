package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.FoodMenu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.ActivityFoodMenuBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class FoodMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()


    }

    private fun initUI() {
        initChart()
        binding.goBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnConfirm.setOnClickListener {
            //TODO GUARDAR ALIMENTO
        }
    }

    private fun initChart() {
        val entryData = ArrayList<PieEntry>()
        entryData.add(PieEntry(70f, "Carbohidratos"))
        entryData.add(PieEntry(30f, "Proteinas"))
        entryData.add(PieEntry(12f, "Grasas"))

        val pieDataSet = PieDataSet(entryData, "Subjects")
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)

        binding.chartCalories.data = PieData(pieDataSet)
        binding.chartCalories.description.isEnabled = false
        binding.chartCalories.centerText = "2000kcal"
        binding.chartCalories.animateY(1000)
        binding.chartCalories.setCenterTextSize(18f)
        binding.chartCalories.setEntryLabelTextSize(14f)

        binding.chartCalories.legend.isEnabled = false


        binding.chartCalories.invalidate()













    }
}