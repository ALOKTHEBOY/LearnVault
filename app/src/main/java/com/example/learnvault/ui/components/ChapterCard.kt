package com.example.learnvault.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learnvault.model.Chapter

@Composable
fun ChapterCard(
    chapter: Chapter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // DERIVED STATE: Calculated purely from the provided chapter data
    val totalTopics = chapter.topics.size
    val completedTopics = chapter.topics.count { it.isCompleted }
    // Prevent division by zero if a chapter is empty
    val progress = if (totalTopics > 0) completedTopics.toFloat() / totalTopics else 0f

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$completedTopics / $totalTopics topics completed",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f)
            )
        }
    }
}