package com.example.onthilaixe.models.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.onthilaixe.models.local.room.entity.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Query("SELECT * FROM ZQUESTION WHERE ZINCLUDEA1 = 1")
    fun getALlQuestionWithLicenseA1(): Flow<List<Question>>
    @Query("SELECT * FROM ZQUESTION WHERE ZINCLUDEA2 = 1")
    fun getALlQuestionWithLicenseA2(): Flow<List<Question>>
    @Query("SELECT * FROM ZQUESTION WHERE ZINCLUDEA34 = 1")
    fun getALlQuestionWithLicenseA34(): Flow<List<Question>>
    @Query("SELECT * FROM ZQUESTION WHERE ZINCLUDEB1 = 1")
    fun getALlQuestionWithLicenseB1(): Flow<List<Question>>
    @Query("SELECT * FROM ZQUESTION WHERE ZINCLUDEB2 = 1")
    fun getALlQuestionWithLicenseB2(): Flow<List<Question>>
    @Query("SELECT * FROM ZQUESTION WHERE ZINCLUDEC = 1")
    fun getALlQuestionWithLicenseC(): Flow<List<Question>>
    @Query("SELECT * FROM ZQUESTION WHERE ZINCLUDEDEF = 1")
    fun getALlQuestionWithLicenseDEF(): Flow<List<Question>>
    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId")
    fun getALlQuestionByType(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE Z_PK IN (:questionIds)")
    fun getQuestionsByQuestionIds(questionIds: List<Int>): Flow<List<Question>>?

    @Query("SELECT * FROM ZQUESTION WHERE Z_PK IN (:questionIds)")
    suspend fun getQuestionsByQuestionIdTest(questionIds: List<Int>): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuestion(question: Question)

    @Query("SELECT * FROM ZQUESTION WHERE ZMARKED != 0")
    fun getQuestionhaveDone() : Flow<List<Question>>

    @Query("UPDATE ZQUESTION SET ZMARKED = 0")
    suspend fun resetAllMarkedStatus()
    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId AND ZINCLUDEA1 = 1")
    fun getALlQuestionByTypeWithLicenseA1(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId AND ZINCLUDEA2 = 1")
    fun getALlQuestionByTypeWithLicenseA2(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId AND ZINCLUDEA34 = 1")
    fun getALlQuestionByTypeWithLicenseA34(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId AND ZINCLUDEB1 = 1")
    fun getALlQuestionByTypeWithLicenseB1(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId AND ZINCLUDEB2 = 1")
    fun getALlQuestionByTypeWithLicenseB2(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId AND ZINCLUDEC = 1")
    fun getALlQuestionByTypeWithLicenseC(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE ZQUESTIONTYPE = :typeId AND ZINCLUDEDEF = 1")
    fun getALlQuestionByTypeWithLicenseDEF(typeId: Int): Flow<List<Question>>

    @Query("SELECT * FROM ZQUESTION WHERE ZWRONG = 1")
    fun getAllQuestionWrong(): Flow<List<Question>>

    @Update
    suspend fun updateItems(items: List<Question>)

    @Query("UPDATE ZQUESTION SET ZMARKED = 0 WHERE Z_PK IN (:ids)")
    suspend fun resetMarkedQuestById(ids: List<Int>)

    @Query("UPDATE ZQUESTION SET ZMARKED = 0, ZWRONG = 0")
    suspend fun resetAll()

    @Query("UPDATE ZQUESTION SET ZMARKED = 0, ZLEARNED = 0 WHERE Z_PK IN (:ids)")
    suspend fun deleteAllQuestionLearned(ids: List<Int>)
    @Query("UPDATE ZTEST SET ISFINISH = 0,ISFAILED = 0 Where IDTEST in (:idTest)")
    suspend fun resetTestDefaultById(idTest: Int)

    @Query("UPDATE ZTEST SET ISFINISH = 0,ISFAILED = 0")
    suspend fun deleteAllTest()

    @Query("UPDATE ZQUESTION SET ZWRONG = 1 WHERE Z_PK IN (:ids)")
    suspend fun updateWrongInQuestion(ids: List<Int>)

    @Query("SELECT * FROM ZQUESTION WHERE Z_PK IN (:questionIds)")
    suspend fun getQuestByQuesIdNoLiveData(questionIds: List<Int>): List<Question>
}