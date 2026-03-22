package com.TFG_JCF.fittrack.ui.MainApp.Diet

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

//@HiltViewModel
//class DietViewModel @Inject constructor(
//    private val dietRepository: DietRepository
//) : ViewModel() {
//
////    private val _meals = MutableStateFlow<List<MealWithItemsAndFoods>>(emptyList())
////    val meals = _meals
////
////    fun loadMeals(uid: String, date: String) {
////
////        viewModelScope.launch {
////
////            val data = dietRepository.getMealsFullByDate(uid, date)
////
////            _meals.value = data
////        }
////    }
//}
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType
import com.TFG_JCF.fittrack.data.model.MealListItem
import kotlinx.coroutines.flow.StateFlow
class DietViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<MealListItem>>(emptyList())
    val items: StateFlow<List<MealListItem>> = _items

    private val _caloriesGoal = MutableStateFlow(2400)
    val caloriesGoal: StateFlow<Int> = _caloriesGoal

    private val _caloriesConsumed = MutableStateFlow(0)
    val caloriesConsumed: StateFlow<Int> = _caloriesConsumed

    private val _caloriesRemaining = MutableStateFlow(2400)
    val caloriesRemaining: StateFlow<Int> = _caloriesRemaining

    fun loadFakeData() {

        val list = listOf(
            MealListItem.Header(MealType.DESAYUNO),
            MealListItem.FoodItem("Avena", 80, 300),
            MealListItem.FoodItem("Huevos", 120, 180),

            MealListItem.Header(MealType.COMIDA),
            MealListItem.FoodItem("Pollo con arroz", 250, 620),

            MealListItem.Header(MealType.CENA),
            MealListItem.FoodItem("Salmón", 180, 350),
            MealListItem.FoodItem("Patata", 150, 140),

            MealListItem.Header(MealType.SNACK),
            MealListItem.FoodItem("Yogur proteico", 200, 160)
        )

        val consumed = list
            .filterIsInstance<MealListItem.FoodItem>()
            .sumOf { it.calories }

        _items.value = list
        _caloriesConsumed.value = consumed
        _caloriesRemaining.value = _caloriesGoal.value - consumed
    }
}