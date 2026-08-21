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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.learnvault.data.preferences.ReadingDensity
import com.example.learnvault.model.Topic
import com.example.learnvault.ui.components.CodeBlock
import com.example.learnvault.ui.components.LearnVaultTopAppBar

@Composable
fun TopicDetailScreen(
    topic: Topic,
    chapterTitle: String,
    readingDensity: ReadingDensity,
    onNavigateBack: () -> Unit,
    onToggleCompletion: () -> Unit,
    onToggleBookmark: () -> Unit,
    onSaveNote: (String) -> Unit
) {
    var isEditingNote by rememberSaveable { mutableStateOf(false) }
    var noteDraft by rememberSaveable(topic.personalNote) { mutableStateOf(topic.personalNote) }

    val verticalSpacing = if (readingDensity == ReadingDensity.COMPACT) 12.dp else 24.dp

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
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(verticalSpacing)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. HEADER & QUICK ACTIONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = topic.shortDescription,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )

                // Accessible Bookmark Icon
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.semantics {
                        contentDescription = if (topic.isBookmarked) "Remove bookmark" else "Bookmark topic"
                    }
                ) {
                    Icon(
                        imageVector = if (topic.isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // 2. KEY TAKEAWAYS (Only renders if the list isn't empty!)
            if (topic.keyTakeaways.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "KEY TAKEAWAYS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    topic.keyTakeaways.forEach { takeaway ->
                        Row(modifier = Modifier.padding(start = 8.dp)) {
                            Text("• ", fontWeight = FontWeight.Bold)
                            Text(text = takeaway, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            // 3. MAIN EXPLANATION
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "EXPLANATION",
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

            // 4. CODE EXAMPLE
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

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // 5. PERSONAL NOTES
            Column(modifier = Modifier.fillMaxWidth()) {
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
                            noteDraft = topic.personalNote
                        }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            onSaveNote(noteDraft.trimEnd())
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

            // 6. COMPLETION ACTION
            Button(
                onClick = onToggleCompletion,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
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
        }
    }
}