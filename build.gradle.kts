// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Add this line. Do not remove your existing plugins!
    alias(libs.plugins.ksp) apply false
}