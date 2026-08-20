package com.example.learnvault.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learnvault.model.Topic
import com.example.learnvault.ui.components.CodeBlock
import com.example.learnvault.ui.components.LearnVaultTopAppBar

@Composable
fun TopicDetailScreen(
    topic: Topic,
    chapterTitle: String,
    onNavigateBack: () -> Unit,
    onToggleCompletion: () -> Unit,
    onToggleBookmark: () -> Unit, // NEW callback
    onSaveNote: (String) -> Unit  // NEW callback
) {
    // TEMPORARY UI STATE for the note editor
    var isEditingNote by remember { mutableStateOf(false) }
    var noteDraft by remember(topic.personalNote) { mutableStateOf(topic.personalNote) }

    Scaffold(
        topBar = {
            LearnVaultTopAppBar(
                title = topic.title,
                subtitle = chapterTitle,
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .imePadding() // <-- NEW: This pushes the screen up when the keyboard appears!
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = topic.shortDescription,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Content
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "CORE CONCEPT",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = topic.explanation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (topic.codeSnippet != null) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "EXAMPLE",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    CodeBlock(code = topic.codeSnippet)
                }
            }

            // Controls (Completion & Bookmark)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 16.dp)) {
                Button(
                    onClick = onToggleCompletion,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (topic.isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (topic.isCompleted) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Completed")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Completed")
                    } else {
                        Text("Mark as Complete")
                    }
                }

                OutlinedButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (topic.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (topic.isBookmarked) "Remove bookmark" else "Bookmark topic"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (topic.isBookmarked) "Bookmarked" else "Add Bookmark")
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // PERSONAL NOTES SECTION
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                Text(
                    text = "My Notes",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (isEditingNote) {
                    OutlinedTextField(
                        value = noteDraft,
                        onValueChange = { noteDraft = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        placeholder = { Text("Write your personal notes here...") }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            isEditingNote = false
                            noteDraft = topic.personalNote // Revert changes
                        }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            onSaveNote(noteDraft.trimEnd()) // NEW: This instantly deletes all trailing empty lines!
                            isEditingNote = false
                        }) {
                            Text("Save Note")
                        }
                    }
                } else {
                    if (topic.personalNote.isNotBlank()) {
                        Text(
                            text = topic.personalNote,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(onClick = { isEditingNote = true }) {
                            Text("Edit Note")
                        }
                    } else {
                        Text(
                            text = "No personal note yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(onClick = { isEditingNote = true }) {
                            Text("Add Note")
                        }
                    }
                }
            }
        }
    }
}