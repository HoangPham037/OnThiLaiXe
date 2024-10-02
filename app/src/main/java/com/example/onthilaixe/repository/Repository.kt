package com.example.onthilaixe.repository

import com.example.onthilaixe.models.local.room.dao.LicenseDao
import com.example.onthilaixe.models.local.room.dao.NoticeBoardDao
import com.example.onthilaixe.models.local.room.dao.QuestionDao
import com.example.onthilaixe.models.local.room.dao.TestDao
import com.example.onthilaixe.models.local.room.dao.TestQuestDao
import com.example.onthilaixe.models.local.room.entity.License
import com.example.onthilaixe.models.local.room.entity.NoticeBoards
import com.example.onthilaixe.models.local.room.entity.Question
import com.example.onthilaixe.models.local.room.entity.Test
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val noticeBoardDao: NoticeBoardDao,
    private val questionDao: QuestionDao,
    private val licenseDao: LicenseDao,
    private val testQuest: TestQuestDao,
    private val testDao: TestDao
) {

    fun getALlQuestionByType(type: Int, chosenInclude: String): Flow<List<Question>> {
        return when (chosenInclude) {
            "A1" -> {
                questionDao.getALlQuestionByTypeWithLicenseA1(type)
            }

            "A2" -> {
                questionDao.getALlQuestionByTypeWithLicenseA2(type)
            }

            "A3", "A4" -> {
                questionDao.getALlQuestionByTypeWithLicenseA34(type)
            }

            "B1" -> {
                questionDao.getALlQuestionByTypeWithLicenseB1(type)
            }

            "B2" -> {
                questionDao.getALlQuestionByTypeWithLicenseB2(type)
            }

            "C" -> {
                questionDao.getALlQuestionByTypeWithLicenseC(type)
            }

            "D", "E", "F" -> {
                questionDao.getALlQuestionByTypeWithLicenseDEF(type)
            }

            else -> {
                throw IllegalArgumentException("Invalid chosenInclude value: $chosenInclude")
            }
        }
    }

    fun getALlQuestionWithLicense(chosenInclude: String): Flow<List<Question>> {
        return when (chosenInclude) {
            "A1" -> {
                questionDao.getALlQuestionWithLicenseA1()
            }

            "A2" -> {
                questionDao.getALlQuestionWithLicenseA2()
            }

            "A3", "A4" -> {
                questionDao.getALlQuestionWithLicenseA34()
            }

            "B1" -> {
                questionDao.getALlQuestionWithLicenseB1()
            }

            "B2" -> {
                questionDao.getALlQuestionWithLicenseB2()
            }

            "C" -> {
                questionDao.getALlQuestionWithLicenseC()
            }

            "D", "E", "F" -> {
                questionDao.getALlQuestionWithLicenseDEF()
            }

            else -> {
                throw IllegalArgumentException("Invalid chosenInclude value: $chosenInclude")
            }
        }
    }

    suspend fun getAllTestQuest(type: Int): List<Int> = testQuest.getAllTestQuest(type)
    suspend fun getAllTestIdByClassLicense(license: String): List<Int> =
        testDao.getAllTestIdByClassLicense(license)

    fun getAllQuestionWrong(): Flow<List<Question>> =
        questionDao.getAllQuestionWrong()

    suspend fun deleteAllQuestionLearned(ids: List<Int>) {
        questionDao.deleteAllQuestionLearned(ids)
    }

    suspend fun saveQuestion(question: Question) {
        questionDao.saveQuestion(question)
    }

    suspend fun resetMarkedQuestById(list: List<Int>) {
        questionDao.resetMarkedQuestById(list)
    }

    suspend fun resetAll() {
        questionDao.resetAll()
    }

    suspend fun updateTestIsFinish(testId: Int?) {
        testDao.updateTestIsFinish(testId)
    }

    suspend fun updateTestIsFailed(testId: Int) {
        testDao.updateTestIsFailed(testId)
    }

    fun getQuestionIdFromTestIds(testId: Int): Flow<List<Int>> =
        testQuest.getQuestionIdFromTestIds(testId)

    fun getQuestionsByQuestionIds(questionIds: List<Int>): Flow<List<Question>>? =
        questionDao.getQuestionsByQuestionIds(questionIds)
    suspend fun getQuestionsByQuestionIdTest(questionIds: List<Int>):List<Question> =
        questionDao.getQuestionsByQuestionIdTest(questionIds)

    fun getAllTestWithClassLicense(license: String): Flow<List<Test>> =
        testDao.getAllTestWithClassLicense(license)

    fun getALlLicense(): Flow<List<License>> =
        licenseDao.getALlLicense()

    fun getALlNoticeBoard(type: Int): Flow<List<NoticeBoards>> =
        noticeBoardDao.getALlNoticeBoardByType(type)

    suspend fun deleteAllTest() {
        questionDao.deleteAllTest()
    }

    suspend fun resetTestDefaultById(idTest: Int) {
        questionDao.resetTestDefaultById(idTest)
    }

    suspend fun updateWrongInQuestion(ids: List<Int>) {
        questionDao.updateWrongInQuestion(ids)
    }
    suspend fun getQuestByQuesIdNoLiveData(questionIds: List<Int>):List<Question> =
        questionDao.getQuestByQuesIdNoLiveData(questionIds)
}