package com.magma.virtuhire.data.remote.request

data class IntroductionRequest(
    val birthday: String,
    val degreeId: Int,
    val institutionId: Int,
    val majorId: Int,
    val educationStartDate: String,
    val educationEndDate: String,
    val skills: List<Int>,
    val cityId: Int,
    val preferredJobTypes: List<String>,
    val expectedSalary: Int,
    val preferredCities: List<Int>,
    val preferredJobCategories: List<Int>
)
