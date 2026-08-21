package com.example.learnvault.ui.viewmodel

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.learnvault.data.local.LearnVaultDatabase
import com.example.learnvault.data.preferences.PreferencesRepository
import com.example.learnvault.data.preferences.ThemeMode
import com.example.learnvault.data.repository.LearnVaultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LearnVaultViewModelTest {

    private lateinit var database: LearnVaultDatabase
    private lateinit var repository: LearnVaultRepository
    private lateinit var viewModel: LearnVaultViewModel
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        // ViewModels use 'viewModelScope' which relies on the Main thread.
        // We must override it for tests to prevent crashes.
        Dispatchers.setMain(UnconfinedTestDispatcher())

        database = Room.inMemoryDatabaseBuilder(context, LearnVaultDatabase::class.java)
            .allowMainThreadQueries().build()

        val testDataStore = PreferenceDataStoreFactory.create(
            produceFile = { File(context.cacheDir, "test_prefs_${UUID.randomUUID()}.preferences_pb") }
        )

        repository = LearnVaultRepository(
            database.topicProgressDao(),
            database.topicPersonalDataDao(),
            PreferencesRepository(testDataStore)
        )

        viewModel = LearnVaultViewModel(repository)
    }

    @After
    fun teardown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun viewModel_initialState_loadsChaptersAndDefaults() = runBlocking {
        // Await the very first emission that contains our chapters
        val initialState = viewModel.uiState.first { it.chapters.isNotEmpty() }

        assertTrue(initialState.chapters.isNotEmpty())
        assertEquals(ThemeMode.SYSTEM, initialState.userPreferences.themeMode)
    }

    @Test
    fun viewModel_toggleCompletion_updatesState() = runBlocking {
        val initialState = viewModel.uiState.first { it.chapters.isNotEmpty() }
        val targetTopicId = initialState.chapters[0].topics[0].id

        // Action
        viewModel.toggleTopicCompletion(targetTopicId)

        // Safely wait until the StateFlow emits a state where this topic is completed
        val updatedState = viewModel.uiState.first { state ->
            state.chapters.flatMap { it.topics }.find { it.id == targetTopicId }?.isCompleted == true
        }

        val updatedTopic = updatedState.chapters.flatMap { it.topics }.find { it.id == targetTopicId }
        assertTrue(updatedTopic!!.isCompleted)
    }

    @Test
    fun viewModel_saveNote_updatesState() = runBlocking {
        val initialState = viewModel.uiState.first { it.chapters.isNotEmpty() }
        val targetTopicId = initialState.chapters[0].topics[0].id

        // Action
        viewModel.savePersonalNote(targetTopicId, "My automated test note")

        // Await the state update containing the note
        val updatedState = viewModel.uiState.first { state ->
            state.chapters.flatMap { it.topics }.find { it.id == targetTopicId }?.personalNote == "My automated test note"
        }

        val updatedTopic = updatedState.chapters.flatMap { it.topics }.find { it.id == targetTopicId }
        assertEquals("My automated test note", updatedTopic!!.personalNote)
    }
}