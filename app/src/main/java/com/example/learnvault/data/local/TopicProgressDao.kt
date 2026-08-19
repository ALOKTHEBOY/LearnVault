package com.example.learnvault.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicProgressDao {

    // Returns a continuous Flow of the database. Whenever a row changes,
    // Room automatically emits a new list!
    @Query("SELECT * FROM topic_progress")
    fun getAllProgress(): Flow<List<TopicProgressEntity>>

    // Inserts a new progress record, or overwrites it if it already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: TopicProgressEntity)
}