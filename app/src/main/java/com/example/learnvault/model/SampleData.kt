package com.example.learnvault.model

object SampleData {
    val chapter1 = Chapter(
        id = "chap_1",
        title = "Chapter 1 — Android Fundamentals",
        topics = listOf(
            Topic(
                id = "top_1_1",
                title = "What is Android?",
                shortDescription = "Understanding the Android OS.",
                explanation = "Android is a mobile operating system based on a modified version of the Linux kernel and other open-source software, designed primarily for touchscreen mobile devices such as smartphones and tablets."
            ),
            Topic(
                id = "top_1_2",
                title = "Android Studio",
                shortDescription = "The official IDE for Android.",
                explanation = "Android Studio provides the fastest tools for building apps on every type of Android device. It includes a robust code editor, debugging tools, and a flexible build system."
            ),
            Topic(
                id = "top_1_3",
                title = "Android Project Structure",
                shortDescription = "How an Android app is organized.",
                explanation = "An Android project contains modules, source code files, and resource files. Key folders include 'java' for code, 'res' for UI resources, and 'manifests' for the AndroidManifest.xml file."
            )
        )
    )

    val chapter2 = Chapter(
        id = "chap_2",
        title = "Chapter 2 — Kotlin Foundations",
        topics = listOf(
            Topic(
                id = "top_2_1",
                title = "Variables",
                shortDescription = "Storing data in Kotlin.",
                explanation = "Kotlin uses two different keywords to declare variables: 'val' for read-only (immutable) variables and 'var' for mutable variables that can be reassigned.",
                codeSnippet = """
                    val appName: String = "LearnVault" // Immutable
                    var version: Int = 1 // Mutable
                    version = 2
                """.trimIndent()
            ),
            Topic(
                id = "top_2_2",
                title = "Functions",
                shortDescription = "Reusable blocks of code.",
                explanation = "Functions in Kotlin are declared using the 'fun' keyword. They can take parameters and return values.",
                codeSnippet = """
                    fun greet(name: String): String {
                        return "Hello, ${'$'}name"
                    }
                """.trimIndent()
            )
        )
    )

    val chapter3 = Chapter(
        id = "chap_3",
        title = "Chapter 3 — Jetpack Compose",
        topics = listOf(
            Topic(
                id = "top_3_1",
                title = "What is Compose?",
                shortDescription = "Android's modern UI toolkit.",
                explanation = "Jetpack Compose is a declarative toolkit for building native Android UI. It simplifies and accelerates UI development using Kotlin."
            ),
            Topic(
                id = "top_3_2",
                title = "Composable Functions",
                shortDescription = "The building blocks of UI.",
                explanation = "To create a Compose UI, you write a Kotlin function and annotate it with '@Composable'. These functions can call other composables to build a hierarchy.",
                codeSnippet = """
                    @Composable
                    fun Greeting(name: String) {
                        Text(text = "Welcome to ${'$'}name!")
                    }
                """.trimIndent()
            )
        )
    )

    // The single list that our application UI will read from
    val chapterList = listOf(chapter1, chapter2, chapter3)
}