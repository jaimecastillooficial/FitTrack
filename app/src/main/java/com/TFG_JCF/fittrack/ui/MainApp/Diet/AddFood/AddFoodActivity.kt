package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.databinding.ActivityAddFoodBinding

class AddFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initUI()


    }

    private fun initUI() {
        binding.goBack.setOnClickListener {
            onBackPressed()
        }

    }

}