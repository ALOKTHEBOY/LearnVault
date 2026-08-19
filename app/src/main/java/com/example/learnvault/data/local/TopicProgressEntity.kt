package com.example.learnvault.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic_progress")
data class TopicProgressEntity(
    @PrimaryKey
    val topicId: String,
    val isCompleted: Boolean
)