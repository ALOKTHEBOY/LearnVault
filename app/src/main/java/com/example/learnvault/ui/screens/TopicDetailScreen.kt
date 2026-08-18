package com.example.learnvault.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    onToggleCompletion: () -> Unit // NEW: Callback to notify the parent when clicked
) {
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

            // NEW: Completion Toggle Button
            Button(
                onClick = onToggleCompletion,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}