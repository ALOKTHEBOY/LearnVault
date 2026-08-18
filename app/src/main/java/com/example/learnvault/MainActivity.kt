package com.example.learnvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    // 1. Create a NavController to command the navigation
    val navController = rememberNavController()

    // 2. Set up the NavHost with our starting screen
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
        // We expect a chapterId in the route so we know which data to load
        composable(
            route = "chapter/{chapterId}",
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")
            val chapter = SampleData.chapterList.find { it.id == chapterId }

            if (chapter != null) {
                ChapterScreen(
                    chapter = chapter,
                    onNavigateBack = { navController.popBackStack() }, // Goes back 1 step
                    onTopicClick = { topicId ->
                        // Pass both IDs so the detail screen knows the parent chapter
                        navController.navigate("topic/$chapterId/$topicId")
                    }
                )
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

            // Look up the specific data
            val chapter = SampleData.chapterList.find { it.id == chapterId }
            val topic = chapter?.topics?.find { it.id == topicId }

            if (chapter != null && topic != null) {
                TopicDetailScreen(
                    topic = topic,
                    chapterTitle = chapter.title, // Supplying the breadcrumb!
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}