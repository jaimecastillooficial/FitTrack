package com.TFG_JCF.fittrack.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.TFG_JCF.fittrack.data.database.dao.Diet.FoodDao
import com.TFG_JCF.fittrack.data.database.dao.Diet.MealDao
import com.TFG_JCF.fittrack.data.database.dao.Diet.MealItemDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.WeightEntryDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.ExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayPlanDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineWeekDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutSetDao

//Diet
import com.TFG_JCF.fittrack.data.database.entities.Diet.FoodEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealEntity
import com.TFG_JCF.fittrack.data.database.entities.Diet.MealItemEntity

//User_Bonus
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity

// Workout
import com.TFG_JCF.fittrack.data.database.entities.Workout.ExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineWeekEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayPlanEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.RoutineDayExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutExerciseEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.WorkoutSetEntity

@Database(
    entities = [
        // User
        UserProfileEntity::class,
        WeightEntryEntity::class,

        // Diet
        FoodEntity::class,
        MealEntity::class,
        MealItemEntity::class,

        // Workout
        ExerciseEntity::class,
        RoutineWeekEntity::class,
        RoutineDayPlanEntity::class,
        RoutineDayExerciseEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        WorkoutSetEntity::class
    ],
    // Cambiar cada vez que se cambie la base de datos
    version = 2,
    exportSchema = false
)
abstract class FitTrackDatabase : RoomDatabase() {

    //TODO Poner los Dao
    abstract fun getFoodDao(): FoodDao
    abstract fun getMealDao(): MealDao
    abstract fun getMealItemDao(): MealItemDao
    abstract fun getUserProfileDao(): UserProfileDao
    abstract fun getWeightEntryDao(): WeightEntryDao
    abstract fun getExerciseDao(): ExerciseDao
    abstract fun getRoutineWeekDao(): RoutineWeekDao
    abstract fun getRoutineDayPlanDao(): RoutineDayPlanDao
    abstract fun getRoutineDayExerciseDao(): RoutineDayExerciseDao
    abstract fun getWorkoutDao(): WorkoutDao
    abstract fun getWorkoutExerciseDao(): WorkoutExerciseDao
    abstract fun getWorkoutSetDao(): WorkoutSetDao




}
