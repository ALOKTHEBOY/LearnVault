package com.example.learnvault.model

data class Chapter(
    val id: String,
    val title: String,
    val topics: List<Topic>
)

data class Topic(
    val id: String,
    val title: String,
    val shortDescription: String,
    val explanation: String,
    val codeSnippet: String? = null,

    // NEW SPRINT 11 FIELDS
    val keyTakeaways: List<String> = emptyList(),
    val visualAssetUri: String? = null, // Architecture placeholder for future visual content

    // PERSISTENT STATE FIELDS (Handled by ViewModel merging)
    val isCompleted: Boolean = false,
    val isBookmarked: Boolean = false,
    val personalNote: String = ""
)