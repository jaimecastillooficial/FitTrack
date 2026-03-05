package com.TFG_JCF.fittrack.data

import com.TFG_JCF.fittrack.data.database.dao.User_Bonus.UserProfileDao
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {

    suspend fun insertUserProfile(user: UserProfileEntity) =
        withContext(Dispatchers.IO) {
            userProfileDao.insertUserProfile(user)
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