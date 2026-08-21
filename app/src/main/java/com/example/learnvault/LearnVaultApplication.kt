package com.example.learnvault

import android.app.Application
import com.example.learnvault.data.local.LearnVaultDatabase
import com.example.learnvault.data.preferences.PreferencesRepository
import com.example.learnvault.data.preferences.dataStore
import com.example.learnvault.data.repository.LearnVaultRepository

class LearnVaultApplication : Application() {
    val database by lazy { LearnVaultDatabase.getDatabase(this) }
    val preferencesRepository by lazy { PreferencesRepository(this.dataStore) } // NEW

    val repository by lazy {
        LearnVaultRepository(
            topicProgressDao = database.topicProgressDao(),
            topicPersonalDataDao = database.topicPersonalDataDao(),
            preferencesRepository = preferencesRepository // NEW
        )
    }
}