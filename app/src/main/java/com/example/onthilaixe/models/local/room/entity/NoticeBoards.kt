package com.example.onthilaixe.models.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TABLE_NOTICE_BOARD")
data class NoticeBoards(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Int? = null,
    @ColumnInfo(name = "Type_ID") val typeId: Int? = null,
    @ColumnInfo(name = "Name") val name: String? = null,
    @ColumnInfo(name = "NameEN") val nameEN: String? = null,
    @ColumnInfo(name = "Detail") val detail: String? = null,
    @ColumnInfo(name = "Icon") val icon: String? = null,
    @ColumnInfo(name = "UpdateDay") val updateDay: String? = null,
    @ColumnInfo(name = "isDelete") val isDelete: Int? = null
)
