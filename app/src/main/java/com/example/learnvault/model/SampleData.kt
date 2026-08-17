package com.example.learnvault.model

object SampleData {
    val androidFundamentals = Chapter(
        id = "chap_1",
        title = "Chapter 1 — Android Fundamentals",
        topics = listOf(
            Topic(
                id = "top_1",
                title = "What is Android?",
                shortDescription = "Understanding the Android OS.",
                explanation = "Android is a mobile operating system based on a modified version of the Linux kernel and other open-source software, designed primarily for touchscreen mobile devices such as smartphones and tablets."
            ),
            Topic(
                id = "top_2",
                title = "Kotlin",
                shortDescription = "The modern language for Android.",
                explanation = "Kotlin is a statically typed programming language running on the JVM. It is fully interoperable with Java and is the recommended language for Android development by Google.",
                codeSnippet = """
                    fun main() {
                        println("Hello, LearnVault!")
                    }
                """.trimIndent()
            )
        )
    )

    // A list of chapters that our UI will read from
    val chapterList = listOf(androidFundamentals)
}