package com.example.learnvault

import android.app.Application
import com.example.learnvault.data.local.LearnVaultDatabase
import com.example.learnvault.data.repository.LearnVaultRepository

class LearnVaultApplication : Application() {
    // We use 'lazy' so the database and repository are only created
    // the first time they are actually needed, saving memory on startup.
    val database by lazy { LearnVaultDatabase.getDatabase(this) }
    val repository by lazy { LearnVaultRepository(database.topicProgressDao()) }
}