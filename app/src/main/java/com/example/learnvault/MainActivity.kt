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
import com.example.learnvault.model.Chapter
import com.example.learnvault.model.Topic
import com.example.learnvault.ui.screens.ChapterScreen
import com.example.learnvault.ui.screens.HomeScreen
import com.example.learnvault.ui.screens.SettingsScreen
import com.example.learnvault.ui.screens.TopicDetailScreen
import com.example.learnvault.ui.state.LearnVaultUiState
import com.example.learnvault.ui.theme.LearnVaultTheme
import com.example.learnvault.ui.viewmodel.LearnVaultViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learnvault.data.preferences.ThemeMode
import com.example.learnvault.data.preferences.ReadingDensity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Just call the app. The app handles its own theme now.
            LearnVaultApp()
        }
    }
}

@Composable
fun LearnVaultApp() {
    val context = androidx.compose.ui.platform.LocalContext.current

    val viewModel: LearnVaultViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val application = context.applicationContext as LearnVaultApplication
                return LearnVaultViewModel(application.repository) as T
            }
        }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isDarkTheme = when (uiState.userPreferences.themeMode) {
        ThemeMode.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    LearnVaultTheme(darkTheme = isDarkTheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        chapters = uiState.chapters,
                        onChapterClick = { chapterId ->
                            navController.navigate("chapter/$chapterId")
                        },
                        onTopicClick = { chapterId, topicId ->
                            navController.navigate("topic/$chapterId/$topicId")
                        },
                        onSettingsClick = { navController.navigate("settings") }
                    )
                }

                composable("settings") {
                    SettingsScreen(
                        userPreferences = uiState.userPreferences,
                        onThemeChange = { viewModel.updateThemeMode(it) },
                        onDensityChange = { viewModel.updateReadingDensity(it) },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = "chapter/{chapterId}",
                    arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val chapterId = backStackEntry.arguments?.getString("chapterId")
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

                    val chapter = uiState.chapters.find { it.id == chapterId }
                    val topic = chapter?.topics?.find { it.id == topicId }

                    if (chapter != null && topic != null) {
                        TopicDetailScreen(
                            topic = topic,
                            chapterTitle = chapter.title,
                            onNavigateBack = { navController.popBackStack() },
                            onToggleCompletion = { viewModel.toggleTopicCompletion(topic.id) },
                            onToggleBookmark = { viewModel.toggleTopicBookmark(topic.id) },
                            onSaveNote = { noteText -> viewModel.savePersonalNote(topic.id, noteText) },
                            readingDensity = uiState.userPreferences.readingDensity // NEW
                        )
                    } else {
                        NotFoundScreen(onNavigateBack = { navController.popBackStack() })
                    }
                }
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