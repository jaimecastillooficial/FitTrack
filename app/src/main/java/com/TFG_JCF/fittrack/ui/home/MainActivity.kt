package com.TFG_JCF.fittrack.ui.home


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val userUid = "TEST_UID"
            val today = "2026-02-13"     // YYYY-MM-DD (pon tu función de fecha si quieres)
            val dayOfWeek = 1            // 1=Lunes (elige tu convención)
            val dayName = "PUSH"
            val routineName = "PPL"

            Log.d("FITTRACK_TEST", "==== START ====")

            // 1) PERFIL (FK)
            val existingProfile = userProfileDao.getByUid(userUid)
            if (existingProfile == null) {
                userProfileDao.insert(
                    UserProfileEntity(
                        uid = userUid,
                        name = "Usuario Prueba",
                        heightCm = 180,
                        goalType = "MANTENIMIENTO",
                        activityLevel = "MEDIO",
                        createdAt = System.currentTimeMillis()
                    )
                )
                Log.d("FITTRACK_TEST", "UserProfile creado uid=$userUid")
            } else {
                Log.d("FITTRACK_TEST", "UserProfile ya existe uid=$userUid")
            }

            // 2) CREAR O REUTILIZAR RUTINA SEMANAL
            // Intentamos obtener la activa; si no existe, creamos una y la activamos.
            var activeRoutine = routineWeekDao.getActiveByUser(userUid)
            val routineWeekId = if (activeRoutine == null) {
                val newId = routineWeekDao.insert(
                    RoutineWeekEntity(
                        userUid = userUid,
                        name = routineName,
                        isActive = true
                    )
                )
                Log.d("FITTRACK_TEST", "RoutineWeek creada id=$newId name=$routineName (active)")
                newId
            } else {
                Log.d("FITTRACK_TEST", "RoutineWeek activa ya existe id=${activeRoutine.id} name=${activeRoutine.name}")
                activeRoutine.id
            }

            // 3) CREAR O REUTILIZAR EL DAY PLAN (Lunes PUSH)
            val existingDayPlan = routineDayPlanDao.getByRoutineWeekAndDay(routineWeekId, dayOfWeek)
            val dayPlanId = if (existingDayPlan == null) {
                val newDayPlanId = routineDayPlanDao.insert(
                    RoutineDayPlanEntity(
                        routineWeekId = routineWeekId,
                        dayOfWeek = dayOfWeek,
                        dayName = dayName,
                        orderIndex = dayOfWeek
                    )
                )
                Log.d("FITTRACK_TEST", "RoutineDayPlan creado id=$newDayPlanId day=$dayOfWeek name=$dayName")
                newDayPlanId
            } else {
                Log.d("FITTRACK_TEST", "RoutineDayPlan ya existe id=${existingDayPlan.id} day=${existingDayPlan.dayOfWeek} name=${existingDayPlan.dayName}")
                existingDayPlan.id
            }

            // 4) CREAR O REUTILIZAR EJERCICIO DE CATÁLOGO
            val exName = "Press Inclinado"
            val exExisting = exerciseDao.getByName(exName)
            val exerciseId = if (exExisting == null) {
                val newExId = exerciseDao.insert(
                    ExerciseEntity(
                        name = exName,
                        muscleGroup = "Pecho",
                        isPublic = true,
                        createdByUid = null
                    )
                )
                Log.d("FITTRACK_TEST", "Exercise creado id=$newExId name=$exName")
                newExId
            } else {
                Log.d("FITTRACK_TEST", "Exercise ya existe id=${exExisting.id} name=${exExisting.name}")
                exExisting.id
            }

            // 5) AÑADIR EJERCICIO AL DÍA DE LA RUTINA (si no está ya)
            val dayExercises = routineDayExerciseDao.getByDayPlan(dayPlanId)
            val alreadyInPlan = dayExercises.any { it.exerciseId == exerciseId }
            if (!alreadyInPlan) {
                val newRdeId = routineDayExerciseDao.insert(
                    RoutineDayExerciseEntity(
                        dayPlanId = dayPlanId,
                        exerciseId = exerciseId,
                        orderIndex = (dayExercises.size + 1)
                    )
                )
                Log.d("FITTRACK_TEST", "RoutineDayExercise añadido id=$newRdeId (exerciseId=$exerciseId) al dayPlanId=$dayPlanId")
            } else {
                Log.d("FITTRACK_TEST", "El ejercicio ya está en el dayPlan")
            }

            // 6) LEER Y MOSTRAR PLAN DEL DÍA
            val planExercises = routineDayExerciseDao.getByDayPlan(dayPlanId)
            Log.d("FITTRACK_TEST", "PLAN $dayName (dayPlanId=$dayPlanId) ejercicios=${planExercises.size}")
            for (pe in planExercises) {
                val e = exerciseDao.getById(pe.exerciseId)
                Log.d("FITTRACK_TEST", " - order=${pe.orderIndex} exerciseId=${pe.exerciseId} name=${e?.name}")
            }

            // 7) SIMULAR BOTÓN "GUARDAR" -> crear Workout del día (1 por día)
            val existingWorkout = workoutDao.getByDate(userUid, today)
            val workoutId = if (existingWorkout == null) {
                val newWorkoutId = workoutDao.insert(
                    WorkoutEntity(
                        userUid = userUid,
                        date = today,
                        dayName = dayName
                    )
                )
                Log.d("FITTRACK_TEST", "Workout creado id=$newWorkoutId date=$today day=$dayName")
                newWorkoutId
            } else {
                Log.d("FITTRACK_TEST", "Workout ya existe id=${existingWorkout.id} date=${existingWorkout.date} day=${existingWorkout.dayName}")
                existingWorkout.id
            }

            // 8) Copiar ejercicios del plan al workout (si no existen dentro del workout)
            val existingWorkoutExercises = workoutExerciseDao.getByWorkout(workoutId)
            for (pe in planExercises) {
                val exists = existingWorkoutExercises.any { it.exerciseId == pe.exerciseId }
                if (!exists) {
                    val newWeId = workoutExerciseDao.insert(
                        WorkoutExerciseEntity(
                            workoutId = workoutId,
                            exerciseId = pe.exerciseId,
                            orderIndex = pe.orderIndex
                        )
                    )
                    Log.d("FITTRACK_TEST", "WorkoutExercise creado id=$newWeId exerciseId=${pe.exerciseId}")
                }
            }

            // 9) LEER WORKOUT COMPLETO Y MOSTRAR (ejercicios + sets)
            val weList = workoutExerciseDao.getByWorkout(workoutId)
            Log.d("FITTRACK_TEST", "WORKOUT $today ($dayName) ejercicios=${weList.size}")

            for (we in weList) {
                val e = exerciseDao.getById(we.exerciseId)
                Log.d("FITTRACK_TEST", " Exercise order=${we.orderIndex} name=${e?.name} (workoutExerciseId=${we.id})")

                val sets = workoutSetDao.getByWorkoutExercise(we.id)
                Log.d("FITTRACK_TEST", "  Sets=${sets.size} (si 0, botón guardar aún no habría guardado sets)")
                for (s in sets) {
                    Log.d("FITTRACK_TEST", "   - Set ${s.setNumber}: ${s.weightKg}kg x ${s.reps} RIR=${s.rir}")
                }
            }

            Log.d("FITTRACK_TEST", "==== END ====")
        }
    }
}
