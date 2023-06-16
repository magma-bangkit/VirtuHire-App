package com.magma.virtuhire.di

import com.magma.virtuhire.data.repository.AuthRepositoryImpl
import com.magma.virtuhire.data.repository.InterviewRepositoryImpl
import com.magma.virtuhire.data.repository.IntroductionRepositoryImpl
import com.magma.virtuhire.data.repository.JobRepositoryImpl
import com.magma.virtuhire.domain.repository.AuthRepository
import com.magma.virtuhire.domain.repository.InterviewRepository
import com.magma.virtuhire.domain.repository.IntroductionRepository
import com.magma.virtuhire.domain.repository.JobRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindJobRepository(
        jobRepositoryImpl: JobRepositoryImpl
    ): JobRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindInterviewRepository(
        interviewRepositoryImpl: InterviewRepositoryImpl
    ): InterviewRepository

    @Binds
    @Singleton
    abstract fun bindIntroductionRepository(
       introductionRepository: IntroductionRepositoryImpl
    ): IntroductionRepository
}