package com.magma.virtuhire.utils

import com.magma.virtuhire.data.remote.response.JobType

fun truncateText(text: String, wordCount: Int): String {
    val words = text.trim().split("\\s+".toRegex())
    return if (words.size > wordCount) {
        words.subList(0, wordCount).joinToString(" ") + "..."
    } else {
        text
    }
}

fun getDisplayText(jobType: JobType): String {
    return when (jobType) {
        JobType.FULL_TIME -> "Full-Time"
        JobType.FREELANCE -> "Freelance"
        JobType.PART_TIME -> "Part-Time"
        JobType.INTERNSHIP -> "Internship"
        JobType.PROJECT_BASED -> "Project-Based"
    }
}

fun getDisplayTextFromString(jobType: String): String {
    return when (jobType) {
        "FULL_TIME" -> "Full-Time"
        "FREELANCE" -> "Freelance"
        "PART_TIME" -> "Part-Time"
        "INTERNSHIP" -> "Internship"
        "PROJECT_BASED" -> "Project-Based"
        else -> ""
    }
}
