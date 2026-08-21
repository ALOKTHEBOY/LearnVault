package com.example.learnvault.data.local

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val dbName = "migration_test_db"
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        // Ensure we start with a perfectly clean slate before testing
        context.deleteDatabase(dbName)
    }

    @After
    fun teardown() {
        // Clean up after the test finishes
        context.deleteDatabase(dbName)
    }

    @Test
    fun migration1To2_preservesExistingProgressData() = runBlocking {
        // ==========================================================
        // STEP 1: Simulate the old Version 1 database
        // ==========================================================
        val factory = FrameworkSQLiteOpenHelperFactory()
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(context)
            .name(dbName)
            .callback(object : SupportSQLiteOpenHelper.Callback(1) { // Version 1
                override fun onCreate(db: SupportSQLiteDatabase) {
                    // Recreate exactly what our V1 table looked like
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `topic_progress` (" +
                                "`topicId` TEXT NOT NULL, " +
                                "`isCompleted` INTEGER NOT NULL, " +
                                "PRIMARY KEY(`topicId`))"
                    )
                }
                override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}
            })
            .build()

        val v1Database = factory.create(configuration).writableDatabase

        // Insert a fake user's hard-earned progress into V1
        v1Database.execSQL("INSERT INTO topic_progress (topicId, isCompleted) VALUES ('topic_1', 1)")
        v1Database.close() // The user "closes" the app to update it


        // ==========================================================
        // STEP 2: Simulate opening the app after updating to Version 2
        // ==========================================================
        val roomDb = Room.databaseBuilder(context, LearnVaultDatabase::class.java, dbName)
            .addMigrations(LearnVaultDatabase.MIGRATION_1_2) // Apply our migration!
            .build()


        // ==========================================================
        // STEP 3: Verify the old data survived
        // ==========================================================
        val progressList = roomDb.topicProgressDao().getAllProgress().first()
        assertEquals("Data should not be deleted during migration", 1, progressList.size)
        assertEquals("topic_1", progressList[0].topicId)
        assertTrue(progressList[0].isCompleted)


        // ==========================================================
        // STEP 4: Verify the new Version 2 table is ready to use
        // ==========================================================
        roomDb.topicPersonalDataDao().insertOrUpdatePersonalData(
            TopicPersonalDataEntity(topicId = "topic_1", isBookmarked = true, personalNote = "Migrated!")
        )

        val personalDataList = roomDb.topicPersonalDataDao().getAllPersonalData().first()
        assertEquals(1, personalDataList.size)
        assertTrue(personalDataList[0].isBookmarked)

        roomDb.close()
    }
}