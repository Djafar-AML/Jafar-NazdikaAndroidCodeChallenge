package com.nazdika.code.challenge.di

import com.nazdika.code.challenge.live_score.repo.MatchesRepo
import com.nazdika.code.challenge.network.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MatchesRepoModule {

    @Provides
    @Singleton
    fun provideRepo(apiClient: ApiClient): MatchesRepo = MatchesRepo(apiClient)

}