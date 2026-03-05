package com.TFG_JCF.fittrack.ui.home


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.TFG_JCF.fittrack.R
import com.TFG_JCF.fittrack.data.database.dao.*
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.ExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineDayPlanDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.RoutineWeekDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutExerciseDao
import com.TFG_JCF.fittrack.data.database.dao.Workout.WorkoutSetDao
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.database.entities.Workout.*

import com.TFG_JCF.fittrack.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userProfileDao: UserProfileDao

    @Inject lateinit var routineWeekDao: RoutineWeekDao
    @Inject lateinit var routineDayPlanDao: RoutineDayPlanDao
    @Inject lateinit var routineDayExerciseDao: RoutineDayExerciseDao

    @Inject lateinit var exerciseDao: ExerciseDao

    @Inject lateinit var workoutDao: WorkoutDao
    @Inject lateinit var workoutExerciseDao: WorkoutExerciseDao
    @Inject lateinit var workoutSetDao: WorkoutSetDao

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }



    }

}

