package com.example.learnvault.data.repository

import com.example.learnvault.data.local.TopicPersonalDataDao
import com.example.learnvault.data.local.TopicPersonalDataEntity
import com.example.learnvault.data.local.TopicProgressDao
import com.example.learnvault.data.local.TopicProgressEntity
import kotlinx.coroutines.flow.Flow

class LearnVaultRepository(
    private val topicProgressDao: TopicProgressDao,
    private val topicPersonalDataDao: TopicPersonalDataDao // NEW DAO
) {

    // --- PROGRESS OPERATIONS ---
    fun getAllProgressStream(): Flow<List<TopicProgressEntity>> =
        topicProgressDao.getAllProgress()

    suspend fun updateTopicProgress(topicId: String, isCompleted: Boolean) {
        val entity = TopicProgressEntity(topicId = topicId, isCompleted = isCompleted)
        topicProgressDao.insertOrUpdateProgress(entity)
    }

    // --- PERSONAL DATA OPERATIONS ---
    fun getAllPersonalDataStream(): Flow<List<TopicPersonalDataEntity>> =
        topicPersonalDataDao.getAllPersonalData()

    suspend fun updateTopicPersonalData(topicId: String, isBookmarked: Boolean, personalNote: String) {
        val entity = TopicPersonalDataEntity(
            topicId = topicId,
            isBookmarked = isBookmarked,
            personalNote = personalNote
        )
        topicPersonalDataDao.insertOrUpdatePersonalData(entity)
    }
}