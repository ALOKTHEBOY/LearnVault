package com.example.learnvault.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnvault.data.local.TopicPersonalDataEntity
import com.example.learnvault.data.local.TopicProgressEntity
import com.example.learnvault.data.preferences.ReadingDensity
import com.example.learnvault.data.preferences.ThemeMode
import com.example.learnvault.data.repository.LearnVaultRepository
import com.example.learnvault.model.SampleData
import com.example.learnvault.ui.state.LearnVaultUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LearnVaultViewModel(
    private val repository: LearnVaultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LearnVaultUiState())
    val uiState: StateFlow<LearnVaultUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAllProgressStream(),
                repository.getAllPersonalDataStream(),
                repository.userPreferencesFlow // NEW STREAM
            ) { progressList, personalDataList, userPrefs ->

                val updatedChapters = SampleData.chapterList.map { chapter ->
                    val updatedTopics = chapter.topics.map { topic ->
                        val savedProgress = progressList.find { it.topicId == topic.id }
                        val savedPersonal = personalDataList.find { it.topicId == topic.id }

                        topic.copy(
                            isCompleted = savedProgress?.isCompleted ?: false,
                            isBookmarked = savedPersonal?.isBookmarked ?: false,
                            personalNote = savedPersonal?.personalNote ?: ""
                        )
                    }
                    chapter.copy(topics = updatedTopics)
                }

                // Return a combined state object
                LearnVaultUiState(chapters = updatedChapters, userPreferences = userPrefs)

            }.collect { newState ->
                _uiState.update { newState }
            }
        }
    }

    // --- EVENT HANDLERS ---

    fun toggleTopicCompletion(topicId: String) {
        val currentTopic = getTopicFromState(topicId) ?: return
        viewModelScope.launch {
            repository.updateTopicProgress(topicId = topicId, isCompleted = !currentTopic.isCompleted)
        }
    }

    fun toggleTopicBookmark(topicId: String) {
        val currentTopic = getTopicFromState(topicId) ?: return
        viewModelScope.launch {
            repository.updateTopicPersonalData(
                topicId = topicId,
                isBookmarked = !currentTopic.isBookmarked,
                personalNote = currentTopic.personalNote
            )
        }
    }

    fun savePersonalNote(topicId: String, note: String) {
        val currentTopic = getTopicFromState(topicId) ?: return
        viewModelScope.launch {
            repository.updateTopicPersonalData(
                topicId = topicId,
                isBookmarked = currentTopic.isBookmarked,
                personalNote = note
            )
        }
    }

    // --- NEW EVENT HANDLERS FOR SETTINGS ---
    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch { repository.updateThemeMode(mode) }
    }

    fun updateReadingDensity(density: ReadingDensity) {
        viewModelScope.launch { repository.updateReadingDensity(density) }
    }

    private fun getTopicFromState(topicId: String) =
        _uiState.value.chapters.flatMap { it.topics }.find { it.id == topicId }
}