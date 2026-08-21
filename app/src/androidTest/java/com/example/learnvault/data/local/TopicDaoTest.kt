package com.example.learnvault.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopicDaoTest {

    private lateinit var database: LearnVaultDatabase
    private lateinit var progressDao: TopicProgressDao
    private lateinit var personalDataDao: TopicPersonalDataDao

    // @Before runs automatically before EVERY test to set up a fresh environment
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Use an in-memory database so it doesn't touch the real app's data
        database = Room.inMemoryDatabaseBuilder(
            context,
            LearnVaultDatabase::class.java
        ).allowMainThreadQueries().build() // Allowed only in testing for speed

        progressDao = database.topicProgressDao()
        personalDataDao = database.topicPersonalDataDao()
    }

    // @After runs automatically after EVERY test to clean up
    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndReadProgress_isSuccessful() = runBlocking {
        // 1. Create a fake progress record
        val progress = TopicProgressEntity(topicId = "topic_1", isCompleted = true)

        // 2. Insert it into the database
        progressDao.insertOrUpdateProgress(progress)

        // 3. Read the Flow emission (using .first() to grab the latest list)
        val flowList = progressDao.getAllProgress().first()

        // 4. Assert (Prove) that the data matches exactly what we expect
        assertEquals(1, flowList.size)
        assertEquals("topic_1", flowList[0].topicId)
        assertTrue(flowList[0].isCompleted)
    }

    @Test
    fun insertAndReadPersonalData_isSuccessful() = runBlocking {
        // 1. Create fake personal data
        val personalData = TopicPersonalDataEntity(
            topicId = "topic_2",
            isBookmarked = true,
            personalNote = "This is a test note."
        )

        // 2. Insert it
        personalDataDao.insertOrUpdatePersonalData(personalData)

        // 3. Read it
        val flowList = personalDataDao.getAllPersonalData().first()

        // 4. Assert (Prove) it saved correctly
        assertEquals(1, flowList.size)
        assertEquals("topic_2", flowList[0].topicId)
        assertTrue(flowList[0].isBookmarked)
        assertEquals("This is a test note.", flowList[0].personalNote)
    }
}