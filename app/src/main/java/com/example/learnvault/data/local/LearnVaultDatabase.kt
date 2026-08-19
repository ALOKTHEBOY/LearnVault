package com.example.learnvault.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TopicProgressEntity::class], version = 1, exportSchema = false)
abstract class LearnVaultDatabase : RoomDatabase() {

    abstract fun topicProgressDao(): TopicProgressDao

    companion object {
        @Volatile
        private var Instance: LearnVaultDatabase? = null

        fun getDatabase(context: Context): LearnVaultDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LearnVaultDatabase::class.java,
                    "learnvault_database"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}