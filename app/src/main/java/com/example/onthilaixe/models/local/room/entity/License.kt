package com.example.onthilaixe.models.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ZLICENSE")
data class License(
    @PrimaryKey (autoGenerate = true) @ColumnInfo(name = "Z_PK") val id: Int?,
    @ColumnInfo(name = "Z_ENT") val entityType: Int?,
    @ColumnInfo(name = "Z_OPT") val option: Int?,
    @ColumnInfo(name = "ZNUMBEROFCORRECTQUESTION") val numberOfCorrectQuestion: Int?,
    @ColumnInfo(name = "ZNUMBEROFQUESTION") val numberOfQuestion: Int?,
    @ColumnInfo(name = "ZNUMBEROFTEST") val numberOfTest: Int?,
    @ColumnInfo(name = "ZDURATION") val duration: Float?,
    @ColumnInfo(name = "ZCONTENT") val content: String?,
    @ColumnInfo(name = "ZDESC") val description: String?,
    @ColumnInfo(name = "ZNAME") val name: String?
)