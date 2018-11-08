package com.alimuzaffar.blank.di.module

import android.app.Application
import androidx.room.Room
import com.alimuzaffar.blank.database.TheDatabase
import com.alimuzaffar.blank.database.dao.SampleDao
import com.alimuzaffar.blank.net.ApiInterface
import com.alimuzaffar.blank.repository.SampleRepository
import dagger.Module
import dagger.Provides
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.inject.Singleton

@Module
class RepositoryModule {

    // Executor providers a background thread for read/write operations.
    @Provides
    internal fun provideExecutor(): ExecutorService {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    internal fun provideScheduledExecutor(): ScheduledExecutorService {
        return Executors.newSingleThreadScheduledExecutor()
    }

    // provides Database
    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): TheDatabase {
        return Room.databaseBuilder(application,
                TheDatabase::class.java, "TheDatabase.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    // Create an injector for each DAO
    @Provides
    @Singleton
    internal fun provideUserDao(database: TheDatabase): SampleDao {
        return database.userDao()
    }


    // provides Repository
    // Create an injector for each Repository
    @Provides
    @Singleton
    internal fun provideUserRepository(apiInterface: ApiInterface, sampleDao: SampleDao, executor: ExecutorService): SampleRepository {
        return SampleRepository(apiInterface, sampleDao, executor)
    }

}
