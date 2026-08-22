package com.example.learnvault.model

object SampleData {
    val chapterList = listOf(
        Chapter(
            id = "chap_1",
            title = "Chapter 1 — Kotlin Foundations",
            topics = listOf(
                Topic(
                    id = "top_1_1",
                    title = "Variables & Mutability",
                    shortDescription = "Storing data safely in Kotlin.",
                    explanation = "Kotlin enforces strict rules on how data changes over time. You declare variables using either 'val' (value) or 'var' (variable). A 'val' cannot be reassigned once initialized, making your code safer and more predictable. A 'var' can be reassigned as often as needed.",
                    codeSnippet = """
                        // Immutable (Cannot be changed)
                        val name: String = "Android Developer"
                        
                        // Mutable (Can be changed)
                        var age: Int = 25
                        age = 26 
                        
                        // Type inference (Kotlin guesses the type)
                        val language = "Kotlin"
                    """.trimIndent(),
                    keyTakeaways = listOf(
                        "Always default to 'val' to prevent accidental data changes.",
                        "Kotlin can automatically infer types, so explicit typing is optional."
                    ),
                    educationalContext = TopicContext(
                        type = ContextType.COMMON_MISTAKE,
                        message = "Trying to reassign a 'val' will cause a compiler error. If a value must change (like a counter), use 'var'."
                    ),
                    // NEW: Visual Asset using a built-in Android drawable name
                    visualAssetUri = "ic_menu_sort_by_size",
                    visualAssetDescription = "Diagram illustrating the difference between a locked box representing an immutable 'val' and an open box representing a mutable 'var'."
                ),
                Topic(
                    id = "top_1_2",
                    title = "Functions",
                    shortDescription = "Writing reusable blocks of code.",
                    explanation = "Functions in Kotlin are declared using the 'fun' keyword. They take parameters with explicit types and declare their return type at the end of the function signature. If a function returns nothing meaningful, its return type is 'Unit' (which can be omitted).",
                    codeSnippet = """
                        fun greetUser(name: String): String {
                            return "Hello, ${'$'}name!"
                        }
                        
                        // Single-expression function
                        fun add(a: Int, b: Int) = a + b
                    """.trimIndent(),
                    keyTakeaways = listOf(
                        "Parameters must always have their types explicitly declared.",
                        "String templates allow you to embed variables directly into strings using the '$' symbol."
                    ),
                    educationalContext = TopicContext(
                        type = ContextType.WHY_IT_MATTERS,
                        message = "Functions keep your code DRY (Don't Repeat Yourself) and make complex logic easier to test in isolation."
                    ),
                    // NEW: Visual Asset
                    visualAssetUri = "ic_menu_manage",
                    visualAssetDescription = "Diagram showing how a Kotlin function receives inputs (parameters) and processes them to produce a specific output."
                ),
                Topic(
                    id = "top_1_3",
                    title = "Android Studio",
                    shortDescription = "The official IDE.",
                    explanation = "Android Studio is the official integrated development environment for Google's Android operating system."
                    // Notice: Intentionally left empty to prove topics without visuals still render perfectly!
                )
            )
        ),
        Chapter(
            id = "chap_2",
            title = "Chapter 2 — Jetpack Compose",
            topics = listOf(
                Topic(
                    id = "top_2_1",
                    title = "What is Compose?",
                    shortDescription = "Modern native Android UI toolkit.",
                    explanation = "Jetpack Compose is Android’s modern declarative UI toolkit. Instead of modifying XML layouts imperatively, you describe what your UI should look like for a given state using Kotlin functions annotated with @Composable.",
                    codeSnippet = """
                        @Composable
                        fun GreetingMessage(name: String) {
                            Text(text = "Welcome to Compose, ${'$'}name!")
                        }
                    """.trimIndent(),
                    keyTakeaways = listOf(
                        "Compose is written entirely in Kotlin.",
                        "UI updates automatically when the underlying state changes.",
                        "Composables are just functions annotated with @Composable."
                    ),
                    educationalContext = TopicContext(
                        type = ContextType.REMEMBER,
                        message = "Composable functions must start with a capital letter (e.g., GreetingMessage, not greetingMessage)."
                    ),
                    // NEW: Visual Asset
                    visualAssetUri = "ic_menu_gallery",
                    visualAssetDescription = "Diagram depicting how UI state dynamically triggers recomposition in Jetpack Compose to redraw the screen."
                )
            )
        )
    )
}