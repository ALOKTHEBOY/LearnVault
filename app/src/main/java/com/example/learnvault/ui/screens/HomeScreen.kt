package com.example.learnvault.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learnvault.model.Chapter
import com.example.learnvault.ui.components.ChapterCard
import com.example.learnvault.ui.components.LearnVaultTopAppBar

@Composable
fun HomeScreen(
    chapters: List<Chapter>,
    onChapterClick: (String) -> Unit
) {
    // DERIVED STATE: Global Library Progress
    val totalTopics = chapters.sumOf { it.topics.size }
    val completedTopics = chapters.sumOf { chapter -> chapter.topics.count { it.isCompleted } }
    val progress = if (totalTopics > 0) completedTopics.toFloat() / totalTopics else 0f

    Scaffold(
        topBar = {
            LearnVaultTopAppBar(title = "LearnVault Library")
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
                    text = "Your Learning Journey",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // NEW: Global Progress Summary
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Overall Progress",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$completedTopics of $totalTopics topics completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            items(chapters) { chapter ->
                ChapterCard(
                    chapter = chapter,
                    onClick = { onChapterClick(chapter.id) }
                )
            }
        }
    }
}