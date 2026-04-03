package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood.FoodMenu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.DietRepository
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealType
import com.TFG_JCF.fittrack.data.model.Diet.FoodMenuUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class FoodMenuViewModel @Inject constructor(
    private val dietRepository: DietRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodMenuUiState())
    val uiState: StateFlow<FoodMenuUiState> = _uiState

    fun loadFood(foodId: Long, mealType: String) {
        viewModelScope.launch {
            val food = dietRepository.getFoodById(foodId)

            if (food == null) {
                return@launch
            }

            val parsedMealType = runCatching { MealType.valueOf(mealType) }
                .getOrElse { MealType.DESAYUNO }

            _uiState.value = FoodMenuUiState(
                foodId = food.id,
                foodName = food.name,
                kcalPer100g = food.kcalPer100g,
                proteinPer100g = food.proteinPer100g,
                carbsPer100g = food.carbsPer100g,
                fatPer100g = food.fatPer100g,
                grams = 100f,
                mealType = parsedMealType
            )

            recalculate()
        }
    }

    fun updateGrams(input: String) {
        val grams = input.toFloatOrNull() ?: 0f
        _uiState.value = _uiState.value.copy(grams = grams)
        recalculate()
    }

    fun updateMealType(mealType: MealType) {
        _uiState.value = _uiState.value.copy(mealType = mealType)
    }

    private fun recalculate() {
        val state = _uiState.value
        val factor = state.grams / 100f
//Copia el objeto y lo modifica con los nuevos valores procesados
        _uiState.value = state.copy(
            calories = (state.kcalPer100g * factor).roundToInt(),
            protein = state.proteinPer100g * factor,
            carbs = state.carbsPer100g * factor,
            fat = state.fatPer100g * factor
        )
    }
//Funcion que recibe 2 lambdas como parametros y las ejecuta cuando se cumpla la condicion x
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveFood(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid.isNullOrEmpty()) {
                    onError("No hay usuario logueado")
                    return@launch
                }

                val state = _uiState.value

                if (state.foodId <= 0L) {
                    onError("Alimento no válido")
                    return@launch
                }

                if (state.grams <= 0f) {
                    onError("Los gramos deben ser mayores que 0")
                    return@launch
                }

                dietRepository.addFoodToMeal(
                    userUid = uid,
                    date = LocalDate.now().toString(),
                    type = state.mealType,
                    foodId = state.foodId,
                    grams = state.grams
                )

                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Error al guardar el alimento")
            }
        }
    }
}