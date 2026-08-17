package com.example.learnvault.model

data class Topic(
    val id: String,
    val title: String,
    val shortDescription: String,
    val explanation: String,
    val codeSnippet: String? = null, // Nullable because not all topics have code
    val isCompleted: Boolean = false
)

data class Chapter(
    val id: String,
    val title: String,
    val topics: List<Topic>
)