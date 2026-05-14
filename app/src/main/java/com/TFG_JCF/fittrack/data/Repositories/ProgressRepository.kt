package com.TFG_JCF.fittrack.data.Repositories

import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.WeightEntryDao
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProgressRepository @Inject constructor(
    private val weightEntryDao: WeightEntryDao,
    private val userProfileDao: UserProfileDao
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
}