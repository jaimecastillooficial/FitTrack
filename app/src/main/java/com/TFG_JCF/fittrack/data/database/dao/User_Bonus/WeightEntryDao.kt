package com.TFG_JCF.fittrack.data.database.dao.User_Bonus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.TFG_JCF.fittrack.data.database.entities.User_Bonus.WeightEntryEntity

@Dao
interface WeightEntryDao {

    // Si ya existe un peso para ese día (índice único), REPLACE lo sustituye
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: WeightEntryEntity)

    @Query("""
        SELECT * FROM weight_entries
        WHERE userUid = :userUid
        ORDER BY date ASC
    """)
    suspend fun getAllByUser(userUid: String): List<WeightEntryEntity>

    @Query("""
        SELECT * FROM weight_entries
        WHERE userUid = :userUid AND date = :date
        LIMIT 1
    """)
    suspend fun getByDate(userUid: String, date: String): WeightEntryEntity?
}
