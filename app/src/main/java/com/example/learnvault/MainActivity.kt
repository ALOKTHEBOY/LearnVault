package com.example.learnvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learnvault.model.SampleData
import com.example.learnvault.ui.screens.ChapterScreen
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
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        // ROUTE 1: Home Screen
        composable("home") {
            HomeScreen(
                chapters = SampleData.chapterList,
                onChapterClick = { chapterId ->
                    navController.navigate("chapter/$chapterId")
                }
            )
        }

        // ROUTE 2: Chapter Screen
        composable(
            route = "chapter/{chapterId}",
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")
            val chapter = SampleData.chapterList.find { it.id == chapterId }

            if (chapter != null) {
                ChapterScreen(
                    chapter = chapter,
                    onNavigateBack = { navController.popBackStack() },
                    onTopicClick = { topicId ->
                        navController.navigate("topic/$chapterId/$topicId")
                    }
                )
            } else {
                // FALLBACK: Chapter not found
                NotFoundScreen(onNavigateBack = { navController.popBackStack() })
            }
        }

        // ROUTE 3: Topic Detail Screen
        composable(
            route = "topic/{chapterId}/{topicId}",
            arguments = listOf(
                navArgument("chapterId") { type = NavType.StringType },
                navArgument("topicId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")
            val topicId = backStackEntry.arguments?.getString("topicId")

            val chapter = SampleData.chapterList.find { it.id == chapterId }
            val topic = chapter?.topics?.find { it.id == topicId }

            if (chapter != null && topic != null) {
                TopicDetailScreen(
                    topic = topic,
                    chapterTitle = chapter.title,
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                // FALLBACK: Topic not found
                NotFoundScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

// ---------------------------------------------------------------------------
// NEW REUSABLE FALLBACK UI
// ---------------------------------------------------------------------------
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