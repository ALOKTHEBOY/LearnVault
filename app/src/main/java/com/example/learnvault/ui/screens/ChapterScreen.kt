package com.example.learnvault.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learnvault.model.Chapter
import com.example.learnvault.ui.components.LearnVaultTopAppBar
import com.example.learnvault.ui.components.TopicCard

@Composable
fun ChapterScreen(
    chapter: Chapter,
    onNavigateBack: () -> Unit,
    onTopicClick: (String) -> Unit
) {
    // DERIVED STATE
    val totalTopics = chapter.topics.size
    val completedTopics = chapter.topics.count { it.isCompleted }
    val progress = if (totalTopics > 0) completedTopics.toFloat() / totalTopics else 0f

    Scaffold(
        topBar = {
            LearnVaultTopAppBar(
                title = "Chapter Overview",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "$completedTopics of $totalTopics topics completed",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // NEW: Chapter Progress Indicator
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.primary,
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(chapter.topics) { topic ->
                TopicCard(
                    topic = topic,
                    onClick = { onTopicClick(topic.id) }
                )
            }
        }
    }
}