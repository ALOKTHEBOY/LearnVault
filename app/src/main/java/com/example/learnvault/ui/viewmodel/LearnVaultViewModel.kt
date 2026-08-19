package com.example.learnvault.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnvault.data.repository.LearnVaultRepository
import com.example.learnvault.model.SampleData
import com.example.learnvault.ui.state.LearnVaultUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LearnVaultViewModel(
    private val repository: LearnVaultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearnVaultUiState())
    val uiState: StateFlow<LearnVaultUiState> = _uiState.asStateFlow()

    init {
        // 1. Start listening to the database the moment the ViewModel is created
        viewModelScope.launch {
            repository.getAllProgressStream().collect { progressList ->

                // 2. MERGE STRATEGY: Combine static SampleData with Room progress
                val updatedChapters = SampleData.chapterList.map { chapter ->
                    val updatedTopics = chapter.topics.map { topic ->
                        // Look for saved progress for this specific topic in the database flow
                        val savedProgress = progressList.find { it.topicId == topic.id }

                        // If found, use its completion state. Otherwise, default to false.
                        topic.copy(isCompleted = savedProgress?.isCompleted ?: false)
                    }
                    chapter.copy(topics = updatedTopics)
                }

                // 3. Emit the newly merged state to the UI
                _uiState.update { currentState ->
                    currentState.copy(chapters = updatedChapters)
                }
            }
        }
    }

    // NEW: Event handler for toggling topic completion
    fun toggleTopicCompletion(topicId: String) {
        // Find the current state of the topic in our UI state
        val currentTopic = _uiState.value.chapters
            .flatMap { it.topics }
            .find { it.id == topicId }

        if (currentTopic != null) {
            val newStatus = !currentTopic.isCompleted

            // Ask the repository to update the database asynchronously.
            // We do NOT manually update _uiState here.
            // The database will save, emit a new list, and our init block will catch it!
            viewModelScope.launch {
                repository.updateTopicProgress(topicId = topicId, isCompleted = newStatus)
            }
        }
    }
}