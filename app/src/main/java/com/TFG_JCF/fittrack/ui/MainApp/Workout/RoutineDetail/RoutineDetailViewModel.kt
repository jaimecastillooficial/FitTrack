package com.TFG_JCF.fittrack.ui.MainApp.Workout.RoutineDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TFG_JCF.fittrack.data.Repositories.RoutineRepository
import com.TFG_JCF.fittrack.data.model.Workout.RoutineDetailItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineDetailViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<RoutineDetailItemUi>>(emptyList())
    val items: StateFlow<List<RoutineDetailItemUi>> = _items.asStateFlow()

    private val _routineName = MutableStateFlow("")
    val routineName: StateFlow<String> = _routineName.asStateFlow()

    fun loadRoutineDetail(routineWeekId: Long) {
        viewModelScope.launch {
            val week = routineRepository.getRoutineWeekById(routineWeekId)
            _routineName.value = week?.name ?: "Rutina"

            val plans = routineRepository.getDaysForWeek(routineWeekId)

            val groupedItems = plans
                .groupBy { it.dayName.trim().uppercase() }
                .map { entry ->
                    val groupedPlans = entry.value.sortedBy { it.dayOfWeek }

                    RoutineDetailItemUi(
                        title = entry.key,
                        selectedDays = groupedPlans.map { it.dayOfWeek },
                        sourceDayPlanIds = groupedPlans.map { it.id }
                    )
                }
                .sortedBy { it.selectedDays.minOrNull() ?: Int.MAX_VALUE }

            _items.value = groupedItems
        }
    }

    fun addBlockToRoutine(
        routineWeekId: Long,
        blockName: String,
        selectedDays: List<Int>,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            //Lmpiar el nombre y verificar que no este vacio
            val cleanName = blockName.trim()

            if (cleanName.isEmpty()) {
                onError("Introduce un nombre para el bloque")
                return@launch
            }

            if (selectedDays.isEmpty()) {
                onError("Selecciona al menos un día")
                return@launch
            }
            //Obtengo los dias ocupados de la semana
            val allPlans = routineRepository.getDaysForWeek(routineWeekId)
            //Validar nombres duplicados
            val blockAlreadyExists = allPlans.any {
                it.dayName.trim().equals(cleanName, ignoreCase = true)
            }

            if (blockAlreadyExists) {
                onError("Ya existe un bloque con ese nombre")
                return@launch
            }
            //miro si esta intentando meter el bloque en un dia ocupado
            val occupiedDays = selectedDays.filter { selectedDay ->
                allPlans.any { existingPlan -> existingPlan.dayOfWeek == selectedDay }
            }
            //si esta intentando meter el bloque en un dia ocupado paso el dia a texto y le digo que esta ocupado
            if (occupiedDays.isNotEmpty()) {
                val occupiedDaysText = occupiedDays.joinToString(", ") { dayNumberToText(it) }
                onError("Estos días ya están ocupados: $occupiedDaysText")
                return@launch
            }
            //creo los dias de entrenamiento NO un bloque es decir creo vario dias luego filtrare para que solo salga una vez
            selectedDays.sorted().forEach { day ->
                routineRepository.createRoutineDay(
                    routineWeekId = routineWeekId,
                    dayOfWeek = day,
                    dayName = cleanName.uppercase()
                )
            }

            loadRoutineDetail(routineWeekId)
            onSuccess()
        }
    }
    fun updateBlockDays(
        routineWeekId: Long,
        blockTitle: String,
        newSelectedDays: List<Int>,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (newSelectedDays.isEmpty()) {
                onError("Selecciona al menos un día")
                return@launch
            }
            //Obtengo todos los planes
            val allPlans = routineRepository.getDaysForWeek(routineWeekId)
            //los filtro por el nombre del bloque
            val currentBlockPlans = allPlans.filter {
                it.dayName.trim().equals(blockTitle.trim(), ignoreCase = true)
            }

            if (currentBlockPlans.isEmpty()) {
                onError("No se ha encontrado el bloque")
                return@launch
            }

            val currentDays = currentBlockPlans.map { it.dayOfWeek }

            val conflictingDays = newSelectedDays.filter { day ->
                allPlans.any { plan ->
                    plan.dayOfWeek == day &&
                            !plan.dayName.trim().equals(blockTitle.trim(), ignoreCase = true)
                }
            }

            if (conflictingDays.isNotEmpty()) {
                val daysText = conflictingDays.joinToString(", ") { dayNumberToText(it) }
                onError("Estos días ya están ocupados: $daysText")
                return@launch
            }

            //Calculo que dias añadir y que dias eliminar
            val daysToAdd = newSelectedDays.filter { it !in currentDays }
            val daysToRemove = currentDays.filter { it !in newSelectedDays }

            daysToRemove.forEach { day ->
                val planToDelete = currentBlockPlans.firstOrNull { it.dayOfWeek == day }
                if (planToDelete != null) {
                    routineRepository.deleteRoutineDay(planToDelete)
                }
            }

            val sourcePlan = currentBlockPlans.firstOrNull { it.dayOfWeek in newSelectedDays }
                ?: currentBlockPlans.first()

            daysToAdd.forEach { day ->
                val newDayPlanId = routineRepository.createRoutineDay(
                    routineWeekId = routineWeekId,
                    dayOfWeek = day,
                    dayName = sourcePlan.dayName
                )
//Por si el usuario ha añadido un dia es decir antes era solo lunes ahora lunes y viernes se copian los ejercicios
                routineRepository.copyExercisesToDay(
                    fromDayPlanId = sourcePlan.id,
                    toDayPlanId = newDayPlanId
                )
            }

            loadRoutineDetail(routineWeekId)
            onSuccess()
        }
    }

    private fun dayNumberToText(day: Int): String {
        return when (day) {
            1 -> "Lunes"
            2 -> "Martes"
            3 -> "Miércoles"
            4 -> "Jueves"
            5 -> "Viernes"
            6 -> "Sábado"
            7 -> "Domingo"
            else -> "-"
        }
    }
}