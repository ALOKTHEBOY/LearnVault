package com.example.learnvault.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnvault.data.local.TopicPersonalDataEntity
import com.example.learnvault.data.local.TopicProgressEntity
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
            // EXPLICIT TYPES ADDED HERE to fix the 24 compiler errors
            combine(
                repository.getAllProgressStream(),
                repository.getAllPersonalDataStream()
            ) { progressList: List<TopicProgressEntity>, personalDataList: List<TopicPersonalDataEntity> ->

                SampleData.chapterList.map { chapter ->
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
            }.collect { updatedChapters ->
                _uiState.update { currentState ->
                    currentState.copy(chapters = updatedChapters)
                }
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

    private fun getTopicFromState(topicId: String) =
        _uiState.value.chapters.flatMap { it.topics }.find { it.id == topicId }
}