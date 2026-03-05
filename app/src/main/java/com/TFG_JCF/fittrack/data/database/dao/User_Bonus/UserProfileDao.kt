package com.TFG_JCF.fittrack.data.database.dao.User_Bonus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(user: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE uid = :uid LIMIT 1")
    suspend fun getUserProfile(uid: String): UserProfileEntity?

    @Update
    suspend fun updateUserProfile(user: UserProfileEntity)

}