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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    chapters: List<Chapter>,
    onChapterClick: (String) -> Unit // Notice this changed from onTopicClick!
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LearnVault Library") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
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
            // New Welcome Header
            item {
                Text(
                    text = "Your Learning Journey",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Select a chapter to begin exploring topics.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Simplified Chapter List
            items(chapters) { chapter ->
                ChapterCard(
                    chapter = chapter,
                    onClick = { onChapterClick(chapter.id) }
                )
            }
        }
    }
}