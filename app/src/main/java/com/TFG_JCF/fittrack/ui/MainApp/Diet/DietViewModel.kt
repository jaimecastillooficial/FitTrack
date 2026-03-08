package com.TFG_JCF.fittrack.ui.MainApp.Diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.DietRepository
import com.TFG_JCF.fittrack.data.database.Relations.Diet.MealWithItemsAndFoods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<MealWithItemsAndFoods>>(emptyList())
    val meals = _meals

    fun loadMeals(uid: String, date: String) {

        viewModelScope.launch {

            val data = dietRepository.getMealsFullByDate(uid, date)

            _meals.value = data
        }
    }
}