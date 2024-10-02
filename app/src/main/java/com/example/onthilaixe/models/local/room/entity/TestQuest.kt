package com.example.onthilaixe.models.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ZTESTQUEST")
data class TestQuest(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ZTESTQUESTID") val ZTESTQUESTID: Int,
    @ColumnInfo(name = "TESTID") val TESTID: Int?=null,
    @ColumnInfo(name = "ZQUESTIONID") val ZQUESTIONID: Int?=null,
    @ColumnInfo(name = "ZANSWER") val ZANSWER: String?=null,
    @ColumnInfo(name = "ZHISTORY") val ZHISTORY: String?=null,
    @ColumnInfo(name = "ZNUMBERWRONG") val ZNUMBERWRONG: Int?=null
)