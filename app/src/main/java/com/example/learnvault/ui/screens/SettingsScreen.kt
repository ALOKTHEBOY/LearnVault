package com.example.learnvault.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.learnvault.data.preferences.ReadingDensity
import com.example.learnvault.data.preferences.ThemeMode
import com.example.learnvault.data.preferences.UserPreferences
import com.example.learnvault.ui.components.LearnVaultTopAppBar

@Composable
fun SettingsScreen(
    userPreferences: UserPreferences,
    onThemeChange: (ThemeMode) -> Unit,
    onDensityChange: (ReadingDensity) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            LearnVaultTopAppBar(
                title = "Settings",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Appearance Section
            Column {
                Text("Appearance", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Column(Modifier.selectableGroup()) {
                    ThemeMode.values().forEach { mode ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (mode == userPreferences.themeMode),
                                    onClick = { onThemeChange(mode) },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (mode == userPreferences.themeMode), onClick = null)
                            Spacer(Modifier.width(16.dp))
                            Text(text = mode.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }

            // Reading Section
            Column {
                Text("Reading", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Column(Modifier.selectableGroup()) {
                    ReadingDensity.values().forEach { density ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .selectable(
                                    selected = (density == userPreferences.readingDensity),
                                    onClick = { onDensityChange(density) },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (density == userPreferences.readingDensity), onClick = null)
                            Spacer(Modifier.width(16.dp))
                            Text(text = density.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }

            // About Section
            Column {
                Text("About", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "LearnVault is your personal Android learning workspace. Progress, notes, and bookmarks are securely stored on this device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}