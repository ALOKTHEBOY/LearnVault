package com.example.learnvault.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryTest {

    private lateinit var preferencesRepository: PreferencesRepository
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        // Create a completely fresh, randomly named DataStore file for EVERY test.
        // This guarantees no test interferes with another.
        val testDataStore = PreferenceDataStoreFactory.create(
            produceFile = { File(context.cacheDir, "test_prefs_${UUID.randomUUID()}.preferences_pb") }
        )
        preferencesRepository = PreferencesRepository(testDataStore)
    }

    @Test
    fun defaultPreferences_areCorrectOnFreshInstall() = runBlocking {
        // 1. Read the very first emission
        val prefs = preferencesRepository.userPreferencesFlow.first()

        // 2. Verify defaults
        assertEquals(ThemeMode.SYSTEM, prefs.themeMode)
        assertEquals(ReadingDensity.COMFORTABLE, prefs.readingDensity)
    }

    @Test
    fun updateThemeMode_savesAndEmitsCorrectly() = runBlocking {
        // 1. Change the theme
        preferencesRepository.updateThemeMode(ThemeMode.DARK)

        // 2. Read the flow
        val prefs = preferencesRepository.userPreferencesFlow.first()

        // 3. Verify it changed
        assertEquals(ThemeMode.DARK, prefs.themeMode)
    }

    @Test
    fun updateReadingDensity_savesAndEmitsCorrectly() = runBlocking {
        // 1. Change the density
        preferencesRepository.updateReadingDensity(ReadingDensity.COMPACT)

        // 2. Read the flow
        val prefs = preferencesRepository.userPreferencesFlow.first()

        // 3. Verify it changed
        assertEquals(ReadingDensity.COMPACT, prefs.readingDensity)
    }
}