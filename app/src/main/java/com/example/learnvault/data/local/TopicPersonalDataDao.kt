package com.example.learnvault.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicPersonalDataDao {

    // Emits a reactive stream of all personal data (bookmarks and notes)
    @Query("SELECT * FROM topic_personal_data")
    fun getAllPersonalData(): Flow<List<TopicPersonalDataEntity>>

    // Inserts or overwrites the personal data for a specific topic
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePersonalData(personalData: TopicPersonalDataEntity)
}