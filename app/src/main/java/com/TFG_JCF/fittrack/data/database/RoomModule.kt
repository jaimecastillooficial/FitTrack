package com.TFG_JCF.fittrack.data.database

import android.content.Context
import androidx.room.Room
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.WeightEntryDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.ExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayPlanDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineWeekDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutSetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//En esta clase cuando daggerhilt este buscando algo relacionado con la BD se encargara
// de proveerle lo que pide para que lo inyecte en la clase que lo necesite

@Module
//Nada que ver con el patron singleton---Indica que el ciclo de vida es el mismo que el de la app
@InstallIn(SingletonComponent::class)

object RoomModule {
    private const val DATABASE_NAME = "FitTrackDatabase"

    //Singleton para que solo se ejecute una vez y no todas las veces que se provea
    @Singleton
    //Indicamos que la funcion proveera algo
    @Provides
    fun provideRoom(@ApplicationContext context: Context)=
        Room.databaseBuilder(context, FitTrackDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFoodDao(db : FitTrackDatabase)= db.getFoodDao()

    @Singleton
    @Provides
    fun provideMealDao(db : FitTrackDatabase)= db.getMealDao()

    @Singleton
    @Provides
    fun provideMealItemDao(db : FitTrackDatabase)= db.getMealItemDao()

    @Singleton
    @Provides
    fun provideUserProfileDao(db: FitTrackDatabase): UserProfileDao = db.getUserProfileDao()

    @Singleton
    @Provides
    fun provideWeightEntryDao(db: FitTrackDatabase): WeightEntryDao = db.getWeightEntryDao()

    @Singleton
    @Provides
    fun provideExerciseDao(db: FitTrackDatabase): ExerciseDao = db.getExerciseDao()

    @Singleton
    @Provides
    fun provideRoutineDayExerciseDao(db: FitTrackDatabase): RoutineDayExerciseDao = db.getRoutineDayExerciseDao()
    @Singleton
    @Provides
    fun provideRoutineDayPlanDao(db: FitTrackDatabase): RoutineDayPlanDao = db.getRoutineDayPlanDao()

    @Singleton
    @Provides
    fun provideRoutineWeekDao(db: FitTrackDatabase): RoutineWeekDao = db.getRoutineWeekDao()

    @Singleton
    @Provides
    fun provideWorkoutDao(db: FitTrackDatabase): WorkoutDao = db.getWorkoutDao()

    @Singleton
    @Provides
    fun provideWorkoutExerciseDao(db: FitTrackDatabase): WorkoutExerciseDao = db.getWorkoutExerciseDao()

    @Singleton
    @Provides
    fun provideWorkoutSetDao(db: FitTrackDatabase): WorkoutSetDao = db.getWorkoutSetDao()


}