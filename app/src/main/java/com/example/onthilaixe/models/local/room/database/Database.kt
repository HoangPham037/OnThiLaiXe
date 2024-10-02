package com.example.onthilaixe.models.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onthilaixe.models.local.room.dao.LicenseDao
import com.example.onthilaixe.models.local.room.dao.NoticeBoardDao
import com.example.onthilaixe.models.local.room.dao.QuestionDao
import com.example.onthilaixe.models.local.room.dao.TestDao
import com.example.onthilaixe.models.local.room.dao.TestQuestDao
import com.example.onthilaixe.models.local.room.entity.License
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.room.entity.Test
import com.example.onthilaixe.models.local.room.entity.TestQuest

@Database(entities = [NoticeBoards::class, Question::class,TestQuest::class,Test::class, License::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun noticeBoardDao(): NoticeBoardDao
    abstract fun questionDao(): QuestionDao
    abstract fun testquest(): TestQuestDao
    abstract fun test() : TestDao
    abstract fun licenseDao(): LicenseDao
}