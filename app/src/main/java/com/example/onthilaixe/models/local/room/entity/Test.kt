package com.example.onthilaixe.models.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ZTEST")
data class Test(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "IDTEST")
    val idTest : Int,
    @ColumnInfo(name = "NAME_TEST")
    val nameTest: Int?=null,
    @ColumnInfo(name = "ZCURRENT_QUEST")
    val zCurrentQuest: Int?=null,
    @ColumnInfo(name = "CLASS_LICENSE")
    val classLicense: String?=null,
    @ColumnInfo(name = "CURRENT_TIME")
    val currentTime: Int?=null,
    @ColumnInfo(name = "TIME_HIS")
    val timeHis: String?=null,
    @ColumnInfo(name = "TOTAL_SUCCESS")
    val totalSuccess: Int?=null,
    @ColumnInfo(name = "ISFINISH")
    val isFinish: Int?=null,
    @ColumnInfo(name = "ISFAILED")
    val isFailed: Int?=null
)


