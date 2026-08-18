package com.example.learnvault.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.learnvault.model.SampleData
import com.example.learnvault.ui.state.LearnVaultUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LearnVaultViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LearnVaultUiState())
    val uiState: StateFlow<LearnVaultUiState> = _uiState.asStateFlow()

    init {
        loadLibraryData()
    }

    private fun loadLibraryData() {
        _uiState.update { currentState ->
            currentState.copy(
                chapters = SampleData.chapterList
            )
        }
    }

    // NEW: Event handler for toggling topic completion
    fun toggleTopicCompletion(topicId: String) {
        _uiState.update { currentState ->
            // Create a completely new list of chapters
            val updatedChapters = currentState.chapters.map { chapter ->
                // Check if this chapter contains the topic we are looking for
                if (chapter.topics.any { it.id == topicId }) {
                    // Create a new list of topics for this specific chapter
                    val updatedTopics = chapter.topics.map { topic ->
                        if (topic.id == topicId) {
                            // Copy the topic and flip its completion boolean
                            topic.copy(isCompleted = !topic.isCompleted)
                        } else {
                            topic // Leave other topics untouched
                        }
                    }
                    // Copy the chapter with the new topics list
                    chapter.copy(topics = updatedTopics)
                } else {
                    chapter // Leave other chapters untouched
                }
            }
            // Update the state with our new immutable chapter list
            currentState.copy(chapters = updatedChapters)
        }
    }
}