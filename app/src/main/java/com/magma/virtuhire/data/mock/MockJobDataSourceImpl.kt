package com.magma.virtuhire.data.mock

import com.magma.virtuhire.domain.model.JobOpening
import javax.inject.Singleton

@Singleton
class MockJobDataSourceImpl : MockJobDataSource {
    override suspend fun getAllJobs(): List<JobOpening> {
        return listOf(
            JobOpening(
                id = "1",
                title = "Software Engineer",
                description = "Lorem ipsum dolor sit amet...",
                company = "ABC Company",
                location = "New York",
                salary = "$100,000",
                postedDate = "June 1, 2023"
            ),
            JobOpening(
                id = "2",
                title = "Product Manager",
                description = "Lorem ipsum dolor sit amet...",
                company = "XYZ Corporation",
                location = "San Francisco",
                salary = "$120,000",
                postedDate = "June 3, 2023"
            ),
            JobOpening(
                id = "3",
                title = "Product Manager",
                description = "Lorem ipsum dolor sit amet...",
                company = "XYZ Corporation",
                location = "San Francisco",
                salary = "$120,000",
                postedDate = "June 3, 2023"
            ),
            JobOpening(
                id = "4",
                title = "Product Manager",
                description = "Lorem ipsum dolor sit amet...",
                company = "XYZ Corporation",
                location = "San Francisco",
                salary = "$120,000",
                postedDate = "June 3, 2023"
            ),
            JobOpening(
                id = "5",
                title = "Product Manager",
                description = "Lorem ipsum dolor sit amet...",
                company = "XYZ Corporation",
                location = "San Francisco",
                salary = "$120,000",
                postedDate = "June 3, 2023"
            ),
            JobOpening(
                id = "6",
                title = "Product Manager",
                description = "Lorem ipsum dolor sit amet...",
                company = "XYZ Corporation",
                location = "San Francisco",
                salary = "$120,000",
                postedDate = "June 3, 2023"
            )

        )
    }
}