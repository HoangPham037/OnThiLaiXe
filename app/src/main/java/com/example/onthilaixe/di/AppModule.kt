package com.example.onthilaixe.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.onthilaixe.models.local.room.dao.LicenseDao
import com.example.onthilaixe.models.local.room.dao.NoticeBoardDao
import com.example.onthilaixe.models.local.room.dao.QuestionDao
import com.example.onthilaixe.models.local.room.dao.TestDao
import com.example.onthilaixe.models.local.room.dao.TestQuestDao
import com.example.onthilaixe.models.local.room.database.Database
import com.example.onthilaixe.repository.Repository
import com.example.onthilaixe.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(
            context,
            Database::class.java,
            Constants.KEY_NAME_DATABASE
        )
            .createFromAsset(Constants.KEY_DATABASE_FILE_PATH)
            .build()

    @Provides
    @Singleton
    fun provideSharedPreference(context: Application): SharedPreferences =
        context.getSharedPreferences(Constants.KEY_PREFS_NAME, Context.MODE_PRIVATE)

    @Provides
    fun providesNoticeBoardDao(db: Database): NoticeBoardDao =
        db.noticeBoardDao()

    @Provides
    fun providesQuestionDao(db: Database): QuestionDao =
        db.questionDao()

    @Provides
    fun providesLicenseDao(db: Database): LicenseDao =
        db.licenseDao()

    @Provides
    fun providesRepository(
        noticeBoardDao: NoticeBoardDao,
        questionDao: QuestionDao,
        testQuestDao: TestQuestDao,
        testDao: TestDao,
        licenseDao: LicenseDao
    ): Repository =
        Repository(noticeBoardDao, questionDao, licenseDao, testQuestDao, testDao)

    @Provides
    fun providesTestQuestDao(db: Database): TestQuestDao =
        db.testquest()

    @Provides
    fun provideTestDao(db: Database): TestDao =
        db.test()
}