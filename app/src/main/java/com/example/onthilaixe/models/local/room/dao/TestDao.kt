package com.example.onthilaixe.models.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.onthilaixe.models.local.room.entity.Test
import kotlinx.coroutines.flow.Flow


@Dao
interface TestDao {
    @Query("SELECT IDTEST FROM ZTEST WHERE CLASS_LICENSE = :classLicense")
    suspend fun getAllTestIdByClassLicense(classLicense: String): List<Int>

    @Query("SELECT * FROM ZTEST WHERE CLASS_LICENSE = :classLicense")
    fun getAllTestWithClassLicense(classLicense: String): Flow<List<Test>>

    @Query("UPDATE ZTEST SET ISFINISH = 1 where IDTEST= :testId")
    suspend fun updateTestIsFinish(testId: Int?)

    @Query("UPDATE ZTEST SET ISFAILED = 1 where IDTEST= :testId")
    suspend fun updateTestIsFailed(testId: Int)

}