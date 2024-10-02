package com.example.onthilaixe.models.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "ZQUESTION")
data class Question(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Z_PK") val pk: Int,
    @ColumnInfo(name = "ZQUESTIONCONTENT") val questionContent: String?,
    @ColumnInfo(name = "ZIMAGE") val image: String?,
    @ColumnInfo(name = "ZOPTION1") val option1: String?,
    @ColumnInfo(name = "ZOPTION2") val option2: String?,
    @ColumnInfo(name = "ZOPTION3") val option3: String?,
    @ColumnInfo(name = "ZOPTION4") val option4: String?,
    @ColumnInfo(name = "ZANSWERDESC") val answerDesc: String?,
    @ColumnInfo(name = "ZANSWERS") val answers: Int?,
    @ColumnInfo(name = "ZQUESTIONTYPE") val questionType: Int?,
    @ColumnInfo(name = "ZLEARNED") var learned: Int?,
    @ColumnInfo(name = "ZMARKED") var marked: Int?,
    @ColumnInfo(name = "ZWRONG") val wrong: Int?,
    @ColumnInfo(name = "ZINCLUDEA1") val includeA1: Int?,
    @ColumnInfo(name = "ZINCLUDEA2") val includeA2: Int?,
    @ColumnInfo(name = "ZINCLUDEA34") val includeA34: Int?,
    @ColumnInfo(name = "ZINCLUDEB1") val includeB1: Int?,
    @ColumnInfo(name = "ZINCLUDEB2") val includeB2: Int?,
    @ColumnInfo(name = "ZINCLUDEC") val includeC: Int?,
    @ColumnInfo(name = "ZINCLUDEDEF") val includeDEF: Int?,
    @ColumnInfo(name = "ZQUESTIONDIE") val questionDie: String?,
    @ColumnInfo(name = "ZAWSA1") val awSA1: String?,
    @ColumnInfo(name = "Z_ENT") val ent: Int?,
)
{
    @Ignore
    var nameTarget: Int? = null
}
