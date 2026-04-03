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
}