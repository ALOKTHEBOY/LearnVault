package com.example.learnvault.model

data class Chapter(
    val id: String,
    val title: String,
    val topics: List<Topic>
)

// NEW: Structured educational context
enum class ContextType { WHY_IT_MATTERS, REMEMBER, COMMON_MISTAKE }

data class TopicContext(
    val type: ContextType,
    val message: String
)

data class Topic(
    val id: String,
    val title: String,
    val shortDescription: String,
    val explanation: String,
    val codeSnippet: String? = null,

    val keyTakeaways: List<String> = emptyList(),
    val visualAssetUri: String? = null,
    val educationalContext: TopicContext? = null, // NEW SPRINT 12 FIELD

    val isCompleted: Boolean = false,
    val isBookmarked: Boolean = false,
    val personalNote: String = ""
)