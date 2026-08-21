package com.example.learnvault.data.preferences

enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class ReadingDensity { COMFORTABLE, COMPACT }

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val readingDensity: ReadingDensity = ReadingDensity.COMFORTABLE
)