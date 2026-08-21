package com.example.learnvault.data.repository

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.learnvault.data.local.LearnVaultDatabase
import com.example.learnvault.data.preferences.PreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class LearnVaultRepositoryTest {

    private lateinit var database: LearnVaultDatabase
    private lateinit var repository: LearnVaultRepository
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        // 1. Setup In-Memory Room Database
        database = Room.inMemoryDatabaseBuilder(context, LearnVaultDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // 2. Setup Test DataStore
        val testDataStore = PreferenceDataStoreFactory.create(
            produceFile = { File(context.cacheDir, "test_prefs_${UUID.randomUUID()}.preferences_pb") }
        )
        val preferencesRepository = PreferencesRepository(testDataStore)

        // 3. Initialize the Main Repository
        repository = LearnVaultRepository(
            topicProgressDao = database.topicProgressDao(),
            topicPersonalDataDao = database.topicPersonalDataDao(),
            preferencesRepository = preferencesRepository
        )
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun repository_savesAndReadsTopicProgress() = runBlocking {
        // Action: Complete a topic
        repository.updateTopicProgress(topicId = "test_topic", isCompleted = true)

        // Verify: The repository's flow emits the correct data
        val progressList = repository.getAllProgressStream().first()

        assertEquals(1, progressList.size)
        assertEquals("test_topic", progressList[0].topicId)
        assertTrue(progressList[0].isCompleted)
    }

    @Test
    fun repository_savesAndReadsPersonalData() = runBlocking {
        // Action: Bookmark and write a note
        repository.updateTopicPersonalData(
            topicId = "test_topic_2",
            isBookmarked = true,
            personalNote = "Repository test note"
        )

        // Verify: The repository successfully stored and emitted the data
        val personalDataList = repository.getAllPersonalDataStream().first()

        assertEquals(1, personalDataList.size)
        assertEquals("test_topic_2", personalDataList[0].topicId)
        assertTrue(personalDataList[0].isBookmarked)
        assertEquals("Repository test note", personalDataList[0].personalNote)
    }
}