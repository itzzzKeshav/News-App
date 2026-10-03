package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.use_case.GetBookmarksUseCase
import com.example.newsapp.domain.use_case.GetNewsUseCase
import com.example.newsapp.domain.use_case.ToggleBookmarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(okHttpClient: OkHttpClient): NewsApi {
        return Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(app: Application): NewsDatabase {
        return Room.databaseBuilder(
            app,
            NewsDatabase::class.java,
            "news_db.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(db: NewsDatabase): ArticleDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: NewsApi,
        dao: ArticleDao
    ): NewsRepository {
        return NewsRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideGetNewsUseCase(repository: NewsRepository): GetNewsUseCase {
        return GetNewsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetBookmarksUseCase(repository: NewsRepository): GetBookmarksUseCase {
        return GetBookmarksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleBookmarkUseCase(repository: NewsRepository): ToggleBookmarkUseCase {
        return ToggleBookmarkUseCase(repository)
    }
}