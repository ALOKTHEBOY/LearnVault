package com.example.learnvault.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// This creates a brand new table alongside our existing progress table
@Entity(tableName = "topic_personal_data")
data class TopicPersonalDataEntity(
    @PrimaryKey
    val topicId: String,
    val isBookmarked: Boolean = false,
    val personalNote: String = ""
)