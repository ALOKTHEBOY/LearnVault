package com.example.learnvault.data.repository

import com.example.learnvault.data.local.TopicProgressDao
import com.example.learnvault.data.local.TopicProgressEntity
import kotlinx.coroutines.flow.Flow

class LearnVaultRepository(private val topicProgressDao: TopicProgressDao) {

    // 1. OBSERVE: Exposes the reactive Flow of data from Room
    fun getAllProgressStream(): Flow<List<TopicProgressEntity>> {
        return topicProgressDao.getAllProgress()
    }

    // 2. UPDATE: Writes to the database off the main UI thread
    suspend fun updateTopicProgress(topicId: String, isCompleted: Boolean) {
        val entity = TopicProgressEntity(topicId = topicId, isCompleted = isCompleted)
        topicProgressDao.insertOrUpdateProgress(entity)
    }
}