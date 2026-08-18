package com.example.learnvault.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.learnvault.model.SampleData
import com.example.learnvault.ui.state.LearnVaultUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LearnVaultViewModel : ViewModel() {

    // 1. PRIVATE MUTABLE STATE: Only the ViewModel can change this.
    private val _uiState = MutableStateFlow(LearnVaultUiState())

    // 2. PUBLIC READ-ONLY STATE: The UI observes this.
    val uiState: StateFlow<LearnVaultUiState> = _uiState.asStateFlow()

    init {
        loadLibraryData()
    }

    private fun loadLibraryData() {
        // In Sprint 4, we are still using static local data.
        // We push SampleData into our StateFlow so the UI can react to it.
        _uiState.update { currentState ->
            currentState.copy(
                chapters = SampleData.chapterList
            )
        }
    }
}