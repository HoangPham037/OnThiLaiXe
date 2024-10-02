package com.example.onthilaixe.models.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface TestQuestDao {
    @Query("SELECT ZQUESTIONID FROM ZTESTQUEST WHERE TESTID = :idTest")
    suspend fun getAllTestQuest(idTest: Int): List<Int>

    @Query("SELECT ZQUESTIONID FROM ZTESTQUEST WHERE TESTID IN (:questionIds)")
    fun getQuestionIdFromTestId(questionIds: List<Int>): Flow<List<Int>>

    @Query("SELECT ZQUESTIONID FROM ZTESTQUEST WHERE TESTID IN (:testId)")
    fun getQuestionIdFromTestIds(testId: Int): Flow<List<Int>>
}