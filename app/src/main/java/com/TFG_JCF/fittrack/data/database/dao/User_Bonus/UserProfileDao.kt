package com.TFG_JCF.fittrack.data.database.dao.User_Bonus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.UserProfileEntity

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE uid = :uid LIMIT 1")
    suspend fun getByUid(uid: String): UserProfileEntity?

}