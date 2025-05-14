package com.example.android.githubaccountfinder.di

import com.example.android.githubaccountfinder.data.network.GitHubApiService
import com.example.android.githubaccountfinder.data.network.GitHubApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GitHubModule {

    @Provides
    @Singleton
    fun provideGitHubRetrofit(): Retrofit {
        return GitHubApiClient.retrofit
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GitHubApiService {
        return retrofit.create(GitHubApiService::class.java)
    }
}