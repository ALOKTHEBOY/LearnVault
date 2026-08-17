package com.example.learnvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.learnvault.model.SampleData
import com.example.learnvault.ui.screens.HomeScreen
import com.example.learnvault.ui.screens.TopicDetailScreen
import com.example.learnvault.ui.theme.LearnVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnVaultTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LearnVaultApp()
                }
            }
        }
    }
}

@Composable
fun LearnVaultApp() {
    // We use a simple String state to track which topic is selected.
    // If it is null, we show the Home Screen.
    var selectedTopicId by remember { mutableStateOf<String?>(null) }

    if (selectedTopicId == null) {
        HomeScreen(
            chapters = SampleData.chapterList,
            onTopicClick = { topicId ->
                selectedTopicId = topicId // Clicking a topic updates the state
            }
        )
    } else {
        // Find the specific topic data based on the ID
        val topic = SampleData.chapterList
            .flatMap { it.topics }
            .find { it.id == selectedTopicId }

        if (topic != null) {
            TopicDetailScreen(
                topic = topic,
                onNavigateBack = { selectedTopicId = null } // Going back resets the state to null
            )
        }
    }
}