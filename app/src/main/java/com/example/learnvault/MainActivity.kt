package com.example.learnvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learnvault.ui.screens.ChapterScreen
import com.example.learnvault.ui.screens.HomeScreen
import com.example.learnvault.ui.screens.TopicDetailScreen
import com.example.learnvault.ui.theme.LearnVaultTheme
import com.example.learnvault.ui.viewmodel.LearnVaultViewModel

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
fun LearnVaultApp(
    // 1. We inject the ViewModel here. The viewModel() function automatically
    // creates it or retrieves the existing one if the screen rotates.
    viewModel: LearnVaultViewModel = viewModel()
) {
    val navController = rememberNavController()

    // 2. OBSERVE STATE: We collect the StateFlow as Compose State.
    // If the ViewModel ever updates the state, this composable will automatically recompose!
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                // 3. We pass data from our observed state, NOT from SampleData directly
                chapters = uiState.chapters,
                onChapterClick = { chapterId ->
                    navController.navigate("chapter/$chapterId")
                }
            )
        }

        composable(
            route = "chapter/{chapterId}",
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")

            // 4. Lookups are now performed against the uiState
            val chapter = uiState.chapters.find { it.id == chapterId }

            if (chapter != null) {
                ChapterScreen(
                    chapter = chapter,
                    onNavigateBack = { navController.popBackStack() },
                    onTopicClick = { topicId ->
                        navController.navigate("topic/$chapterId/$topicId")
                    }
                )
            } else {
                NotFoundScreen(onNavigateBack = { navController.popBackStack() })
            }
        }

        composable(
            route = "topic/{chapterId}/{topicId}",
            arguments = listOf(
                navArgument("chapterId") { type = NavType.StringType },
                navArgument("topicId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")
            val topicId = backStackEntry.arguments?.getString("topicId")

            // 5. Lookups are now performed against the uiState
            val chapter = uiState.chapters.find { it.id == chapterId }
            val topic = chapter?.topics?.find { it.id == topicId }

            if (chapter != null && topic != null) {
                TopicDetailScreen(
                    topic = topic,
                    chapterTitle = chapter.title,
                    onNavigateBack = { navController.popBackStack() },
                    // NEW: We pass the event up to the ViewModel!
                    onToggleCompletion = { viewModel.toggleTopicCompletion(topic.id) }
                )
            } else {
                NotFoundScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun NotFoundScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Content not found",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The learning material you are looking for is missing or invalid.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateBack) {
            Text("Go Back")
        }
    }
}