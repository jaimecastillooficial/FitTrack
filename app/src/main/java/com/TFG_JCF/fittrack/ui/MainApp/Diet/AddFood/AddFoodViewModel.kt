package com.TFG_JCF.fittrack.ui.MainApp.Diet.AddFood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.DietRepository
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFoodViewModel @Inject constructor(
    private val repository: DietRepository
) : ViewModel() {

    private var fullFoodList: List<FoodEntity> = emptyList()

    private val _filteredFoods = MutableLiveData<List<FoodEntity>>()

    val filteredFoods: LiveData<List<FoodEntity>> = _filteredFoods

    val defaultFoods = listOf(

        // PROTEÍNAS
        FoodEntity(name = "Pechuga de pollo", kcalPer100g = 165, proteinPer100g = 31f, carbsPer100g = 0f, fatPer100g = 3.6f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Pavo", kcalPer100g = 135, proteinPer100g = 29f, carbsPer100g = 0f, fatPer100g = 1f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Ternera", kcalPer100g = 250, proteinPer100g = 26f, carbsPer100g = 0f, fatPer100g = 15f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Cerdo", kcalPer100g = 242, proteinPer100g = 27f, carbsPer100g = 0f, fatPer100g = 14f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Salmón", kcalPer100g = 208, proteinPer100g = 20f, carbsPer100g = 0f, fatPer100g = 13f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Atún", kcalPer100g = 132, proteinPer100g = 28f, carbsPer100g = 0f, fatPer100g = 1f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Huevo", kcalPer100g = 155, proteinPer100g = 13f, carbsPer100g = 1.1f, fatPer100g = 11f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Clara de huevo", kcalPer100g = 52, proteinPer100g = 11f, carbsPer100g = 0.7f, fatPer100g = 0.2f, isPublic = true, createdByUid = null),

        // LÁCTEOS
        FoodEntity(name = "Leche entera", kcalPer100g = 60, proteinPer100g = 3.2f, carbsPer100g = 5f, fatPer100g = 3.2f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Leche desnatada", kcalPer100g = 34, proteinPer100g = 3.4f, carbsPer100g = 5f, fatPer100g = 0.1f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Yogur natural", kcalPer100g = 59, proteinPer100g = 10f, carbsPer100g = 3.6f, fatPer100g = 0.4f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Queso fresco", kcalPer100g = 98, proteinPer100g = 11f, carbsPer100g = 3f, fatPer100g = 4f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Queso curado", kcalPer100g = 402, proteinPer100g = 25f, carbsPer100g = 1.3f, fatPer100g = 33f, isPublic = true, createdByUid = null),

        // CARBOHIDRATOS
        FoodEntity(name = "Arroz blanco", kcalPer100g = 130, proteinPer100g = 2.7f, carbsPer100g = 28f, fatPer100g = 0.3f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Arroz integral", kcalPer100g = 111, proteinPer100g = 2.6f, carbsPer100g = 23f, fatPer100g = 0.9f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Pasta", kcalPer100g = 131, proteinPer100g = 5f, carbsPer100g = 25f, fatPer100g = 1.1f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Pan blanco", kcalPer100g = 265, proteinPer100g = 9f, carbsPer100g = 49f, fatPer100g = 3.2f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Pan integral", kcalPer100g = 247, proteinPer100g = 13f, carbsPer100g = 41f, fatPer100g = 4.2f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Avena", kcalPer100g = 389, proteinPer100g = 17f, carbsPer100g = 66f, fatPer100g = 7f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Quinoa", kcalPer100g = 120, proteinPer100g = 4f, carbsPer100g = 21f, fatPer100g = 2f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Patata", kcalPer100g = 77, proteinPer100g = 2f, carbsPer100g = 17f, fatPer100g = 0.1f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Boniato", kcalPer100g = 86, proteinPer100g = 1.6f, carbsPer100g = 20f, fatPer100g = 0.1f, isPublic = true, createdByUid = null),

        // LEGUMBRES
        FoodEntity(name = "Lentejas", kcalPer100g = 116, proteinPer100g = 9f, carbsPer100g = 20f, fatPer100g = 0.4f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Garbanzos", kcalPer100g = 164, proteinPer100g = 9f, carbsPer100g = 27f, fatPer100g = 2.6f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Alubias", kcalPer100g = 127, proteinPer100g = 9f, carbsPer100g = 22f, fatPer100g = 0.5f, isPublic = true, createdByUid = null),

        // FRUTAS
        FoodEntity(name = "Plátano", kcalPer100g = 89, proteinPer100g = 1.1f, carbsPer100g = 23f, fatPer100g = 0.3f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Manzana", kcalPer100g = 52, proteinPer100g = 0.3f, carbsPer100g = 14f, fatPer100g = 0.2f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Pera", kcalPer100g = 57, proteinPer100g = 0.4f, carbsPer100g = 15f, fatPer100g = 0.1f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Naranja", kcalPer100g = 47, proteinPer100g = 0.9f, carbsPer100g = 12f, fatPer100g = 0.1f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Fresas", kcalPer100g = 32, proteinPer100g = 0.7f, carbsPer100g = 7.7f, fatPer100g = 0.3f, isPublic = true, createdByUid = null),

        // VERDURAS
        FoodEntity(name = "Brócoli", kcalPer100g = 34, proteinPer100g = 2.8f, carbsPer100g = 7f, fatPer100g = 0.4f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Zanahoria", kcalPer100g = 41, proteinPer100g = 0.9f, carbsPer100g = 10f, fatPer100g = 0.2f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Tomate", kcalPer100g = 18, proteinPer100g = 0.9f, carbsPer100g = 3.9f, fatPer100g = 0.2f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Lechuga", kcalPer100g = 15, proteinPer100g = 1.4f, carbsPer100g = 2.9f, fatPer100g = 0.2f, isPublic = true, createdByUid = null),

        // GRASAS
        FoodEntity(name = "Aceite de oliva", kcalPer100g = 884, proteinPer100g = 0f, carbsPer100g = 0f, fatPer100g = 100f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Mantequilla", kcalPer100g = 717, proteinPer100g = 0.9f, carbsPer100g = 0.1f, fatPer100g = 81f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Almendras", kcalPer100g = 579, proteinPer100g = 21f, carbsPer100g = 22f, fatPer100g = 50f, isPublic = true, createdByUid = null),
        FoodEntity(name = "Nueces", kcalPer100g = 654, proteinPer100g = 15f, carbsPer100g = 14f, fatPer100g = 65f, isPublic = true, createdByUid = null)
    )

    fun insertDefaultFoodsIfNeeded() {
        viewModelScope.launch {
            val current = repository.getAllFoods()

            if (current.isEmpty()) {
                repository.insertFoods(defaultFoods)
            }
        }
    }

    fun loadFoods() {
        viewModelScope.launch {
            fullFoodList = repository.getAllFoods()
            _filteredFoods.postValue(fullFoodList)
        }
    }

    fun filterFoods(query: String) {
        if (query.isBlank()) {
            _filteredFoods.value = fullFoodList
            return
        }

        val filteredList = fullFoodList.filter { food ->
            food.name.contains(query, ignoreCase = true)
        }

        _filteredFoods.value = filteredList
    }
}