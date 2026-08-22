package com.example.learnvault.ui.utils

import android.content.Context

object VisualAssetResolver {
    /**
     * Resolves a domain-level string URI into a local drawable resource ID.
     * Uses the app's package name to ensure we own the asset.
     */
    fun resolveDrawableId(context: Context, assetUri: String?): Int? {
        if (assetUri.isNullOrBlank()) return null

        // Notice we use context.packageName instead of "android"!
        val id = context.resources.getIdentifier(assetUri, "drawable", context.packageName)
        return if (id != 0) id else null
    }
}