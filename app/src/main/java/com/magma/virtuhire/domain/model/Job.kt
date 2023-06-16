package com.magma.virtuhire.domain.model

data class JobOpening(
    val id: String,
    val title: String,
    val description: String,
    val company: String,
    val location: String,
    val salary: String,
    val postedDate: String
)
