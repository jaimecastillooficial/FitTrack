package com.TFG_JCF.fittrack.data.Repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.TFG_JCF.fittrack.data.database.dao.Diet.MealDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.WeightEntryDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutDao
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import com.TFG_JCF.fittrack.data.model.Progress.DietCaloriesChart
import com.TFG_JCF.fittrack.data.model.Progress.WorkoutWeekChart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ProgressRepository @Inject constructor(
    private val weightEntryDao: WeightEntryDao,
    private val userProfileDao: UserProfileDao,
    private val mealDao: MealDao,
    private val workoutDao: WorkoutDao
) {

    suspend fun getWeightEntriesByUser(userUid: String): List<WeightEntryEntity> {
        return withContext(Dispatchers.IO) {
            weightEntryDao.getAllByUser(userUid)
        }
    }

    suspend fun insertWeightEntry(entry: WeightEntryEntity) {
        withContext(Dispatchers.IO) {
            weightEntryDao.insert(entry)

            val profile = userProfileDao.getUserProfile(entry.userUid)

            if (profile != null) {
                userProfileDao.updateUserProfile(
                    profile.copy(currentWeight = entry.weightKg)
                )
            }
        }
    }

    suspend fun deleteWeightEntry(entry: WeightEntryEntity) {
        withContext(Dispatchers.IO) {
            weightEntryDao.delete(entry)
        }
    }

    suspend fun getUserProfile(userUid: String): UserProfileEntity? {
        return withContext(Dispatchers.IO) {
            userProfileDao.getUserProfile(userUid)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCaloriesLast7Days(userUid: String): List<DietCaloriesChart> {
        return withContext(Dispatchers.IO) {
            val today = LocalDate.now()
            val startDate = today.minusDays(6)

            val result = mutableListOf<DietCaloriesChart>()

            for (i in 0..6) {
                val date = startDate.plusDays(i.toLong())
                val dateString = date.toString()

                val meals = mealDao.getMealsWithItemsAndFoodsByDate(
                    userUid = userUid,
                    date = dateString
                )

                val totalCalories = meals.sumOf { meal ->
                    meal.items.sumOf { itemWithFood ->
                        val food = itemWithFood.food
                        val grams = itemWithFood.item.grams

                        ((food.kcalPer100g * grams) / 100f).toDouble()
                    }
                }.toFloat()

                result.add(
                    DietCaloriesChart(
                        date = dateString,
                        calories = totalCalories
                    )
                )
            }

            result
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getWorkoutWeeksLast4Weeks(userUid: String): List<WorkoutWeekChart> {
        return withContext(Dispatchers.IO) {
            val today = LocalDate.now()
            val startDate = today.minusDays(27)

            val workouts = workoutDao.getWorkoutsFullByRange(
                userUid = userUid,
                fromDate = startDate.toString(),
                toDate = today.toString()
            )

            val labels = listOf("Hace 3", "Hace 2", "Anterior", "Actual")
            val result = mutableListOf<WorkoutWeekChart>()

            for (weekIndex in 0..3) {
                val weekStart = startDate.plusDays((weekIndex * 7).toLong())
                val weekEnd = weekStart.plusDays(6)

                val count = workouts.count { workoutFull ->
                    val workoutDate = LocalDate.parse(workoutFull.workout.date)
                    !workoutDate.isBefore(weekStart) && !workoutDate.isAfter(weekEnd)
                }

                result.add(
                    WorkoutWeekChart(
                        label = labels[weekIndex],
                        workoutCount = count
                    )
                )
            }

            result
        }
    }
}