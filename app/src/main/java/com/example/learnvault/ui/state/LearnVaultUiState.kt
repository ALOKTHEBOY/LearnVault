package com.example.learnvault.ui.state

import com.example.learnvault.model.Chapter
import com.example.learnvault.data.preferences.UserPreferences

data class LearnVaultUiState(
    val chapters: List<Chapter> = emptyList(),
    val userPreferences: UserPreferences = UserPreferences() // NEW
)