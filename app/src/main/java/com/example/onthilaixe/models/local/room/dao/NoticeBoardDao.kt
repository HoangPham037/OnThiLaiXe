package com.example.onthilaixe.models.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.onthilaixe.models.local.room.entity.License
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeBoardDao {
    @Query("SELECT * FROM TABLE_NOTICE_BOARD WHERE Type_ID = :typeId")
    fun getALlNoticeBoardByType(typeId: Int): Flow<List<NoticeBoards>>
}