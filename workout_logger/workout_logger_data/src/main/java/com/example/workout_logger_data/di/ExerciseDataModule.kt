package com.example.workout_logger_data.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.workout_logger_data.local.ExerciseDatabase
import com.example.workout_logger_data.repository.ExerciseRepositoryImpl
import com.example.workout_logger_domain.repository.ExerciseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExerciseDataModule {

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(
//                HttpLoggingInterceptor().apply {
//                    level = HttpLoggingInterceptor.Level.BODY
//                }
//            )
//            .build()
//    }

    @Provides
    @Singleton
    fun provideExerciseDatabase(app: Application): ExerciseDatabase {

        return Room.databaseBuilder(
            app,
            ExerciseDatabase::class.java,
            "exercise_db"
        )
            .createFromAsset("database/exercise.db")
//            .fallbackToDestructiveMigration()
//            .createFromAsset("database/muscles.tsv")
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(
        db: ExerciseDatabase
    ): ExerciseRepository {
        return ExerciseRepositoryImpl(
            dao = db.dao
        )
    }
}