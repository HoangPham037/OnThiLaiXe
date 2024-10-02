package com.example.onthilaixe.models.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.onthilaixe.models.local.room.entity.License
import kotlinx.coroutines.flow.Flow
@Dao
interface LicenseDao {
    @Query("SELECT ZDURATION FROM ZLICENSE WHERE ZNAME = :nameLicense")
    suspend fun getDurationByLicenseName(nameLicense: String) : Float

    @Query("SELECT * FROM ZLICENSE")
    fun getALlLicense(): Flow<List<License>>

    @Query("SELECT * FROM ZLICENSE WHERE ZNAME = :licenseName")
    fun getLicenseWithName(licenseName: String) : Flow<License>

    @Query("SELECT * FROM ZLICENSE WHERE ZNAME = :licensename")
    fun deletemelater(licensename: String) : License

}