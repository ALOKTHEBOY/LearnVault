package com.example.learnvault.data.repository

import com.example.learnvault.data.local.TopicPersonalDataDao
import com.example.learnvault.data.local.TopicPersonalDataEntity
import com.example.learnvault.data.local.TopicProgressDao
import com.example.learnvault.data.local.TopicProgressEntity
import com.example.learnvault.data.preferences.PreferencesRepository
import com.example.learnvault.data.preferences.ReadingDensity
import com.example.learnvault.data.preferences.ThemeMode
import com.example.learnvault.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

class LearnVaultRepository(
    private val topicProgressDao: TopicProgressDao,
    private val topicPersonalDataDao: TopicPersonalDataDao,
    private val preferencesRepository: PreferencesRepository // NEW
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

    // --- PREFERENCES OPERATIONS ---
    val userPreferencesFlow: Flow<UserPreferences> = preferencesRepository.userPreferencesFlow

    suspend fun updateThemeMode(mode: ThemeMode) = preferencesRepository.updateThemeMode(mode)

    suspend fun updateReadingDensity(density: ReadingDensity) = preferencesRepository.updateReadingDensity(density)
}