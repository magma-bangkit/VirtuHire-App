package com.magma.virtuhire.data.mock

import com.magma.virtuhire.domain.model.JobOpening

interface MockJobDataSource {
    suspend fun getAllJobs(): List<JobOpening>
}