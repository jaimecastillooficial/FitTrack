package com.TFG_JCF.fittrack.data.Repositories

import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.WeightEntryDao
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val weightEntryDao: WeightEntryDao
) {

    suspend fun insertUserProfile(user: UserProfileEntity) =
        withContext(Dispatchers.IO) {
            userProfileDao.insertUserProfile(user)
        }

    suspend fun insertUserProfileWithInitialWeight(user: UserProfileEntity) =
        withContext(Dispatchers.IO) {
            userProfileDao.insertUserProfile(user)

            val today = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())

            weightEntryDao.insert(
                WeightEntryEntity(
                    userUid = user.uid,
                    date = today,
                    weightKg = user.currentWeight
                )
            )
        }

    suspend fun getUserProfile(uid: String): UserProfileEntity? =
        withContext(Dispatchers.IO) {
            userProfileDao.getUserProfile(uid)
        }

    suspend fun updateUserProfile(user: UserProfileEntity) =
        withContext(Dispatchers.IO) {
            userProfileDao.updateUserProfile(user)
        }

}