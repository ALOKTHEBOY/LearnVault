package com.example.learnvault.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// 1. INCREASE VERSION TO 2 and add the new Entity class to the array
@Database(
    entities = [TopicProgressEntity::class, TopicPersonalDataEntity::class],
    version = 2,
    exportSchema = false
)
abstract class LearnVaultDatabase : RoomDatabase() {

    // Existing DAO
    abstract fun topicProgressDao(): TopicProgressDao

    // NEW DAO
    abstract fun topicPersonalDataDao(): TopicPersonalDataDao

    companion object {
        @Volatile
        private var Instance: LearnVaultDatabase? = null

        // 2. DEFINE THE MIGRATION SCRIPT (Version 1 -> 2)
        // This tells SQLite exactly how to alter the database without deleting old data.
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // We create the new table. Notice how SQLite maps Boolean to INTEGER (0 or 1).
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `topic_personal_data` (" +
                            "`topicId` TEXT NOT NULL, " +
                            "`isBookmarked` INTEGER NOT NULL, " +
                            "`personalNote` TEXT NOT NULL, " +
                            "PRIMARY KEY(`topicId`))"
                )
            }
        }

        fun getDatabase(context: Context): LearnVaultDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LearnVaultDatabase::class.java,
                    "learnvault_database"
                )
                    // 3. REGISTER THE MIGRATION WITH ROOM
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}