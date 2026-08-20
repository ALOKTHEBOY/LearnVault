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
    val isCompleted: Boolean = false,

    // NEW SPRINT 7 FIELDS
    val isBookmarked: Boolean = false,
    val personalNote: String = ""
)