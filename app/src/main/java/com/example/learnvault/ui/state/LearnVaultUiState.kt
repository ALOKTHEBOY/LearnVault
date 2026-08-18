package com.example.learnvault.ui.state

import com.example.learnvault.model.Chapter

// A simple data class representing the state of our learning library.
// It is completely immutable (using 'val'), meaning the UI can only read it, never change it directly.
data class LearnVaultUiState(
    val chapters: List<Chapter> = emptyList()
)