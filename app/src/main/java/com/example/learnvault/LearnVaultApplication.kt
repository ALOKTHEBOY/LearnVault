package com.example.learnvault

import android.app.Application
import com.example.learnvault.data.local.LearnVaultDatabase
import com.example.learnvault.data.repository.LearnVaultRepository

class LearnVaultApplication : Application() {
    val database by lazy { LearnVaultDatabase.getDatabase(this) }

    val repository by lazy {
        LearnVaultRepository(
            topicProgressDao = database.topicProgressDao(),
            topicPersonalDataDao = database.topicPersonalDataDao() // Injecting the new DAO here
        )
    }
}