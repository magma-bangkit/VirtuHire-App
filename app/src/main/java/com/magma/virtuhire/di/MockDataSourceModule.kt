package com.magma.virtuhire.di

import com.magma.virtuhire.data.mock.MockInterviewDataSource
import com.magma.virtuhire.data.mock.MockInterviewDataSourceImpl
import com.magma.virtuhire.data.mock.MockJobDataSource
import com.magma.virtuhire.data.mock.MockJobDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MockDataSourceModule {

    @Provides
    fun provideJobDataSource(): MockJobDataSource {
        return MockJobDataSourceImpl()
    }

    @Provides
    fun provideInterviewDataSource(): MockInterviewDataSource {
        return MockInterviewDataSourceImpl()
    }
}