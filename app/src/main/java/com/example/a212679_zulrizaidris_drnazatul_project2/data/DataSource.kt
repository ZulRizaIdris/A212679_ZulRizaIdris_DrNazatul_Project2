package com.example.a212679_zulrizaidris_drnazatul_project2.data

import com.example.a212679_zulrizaidris_drnazatul_project2.R

// 1. description is now a List of Strings
data class JobOpening(
    val title: String,
    val company: String,
    val location: String,
    val type: String,
    val salary: String,
    val logoRes: Int,
    val description: List<String>
)

object DataSource {
    val interviewDates = listOf(
        "Mon, May 18", "Tue, May 19", "Wed, May 20"
    )

    val skills = listOf(
        "Team Building", "Information Technology", "Thoroughness",
        "Java", "PHP", "SQL", "Cisco"
    )

    val jobOpenings = listOf(
        JobOpening(
            title = "Software Engineer",
            company = "Genetec Technology Berhad",
            location = "Cyberjaya, Selangor",
            type = "Full time",
            salary = "RM 5,000 - RM 7,000 per month",
            logoRes = R.drawable.genetec_logo,
            description = listOf(
                "Design, develop, and install software solutions.",
                "Build high-quality, innovative, and fully performing software.",
                "Ensure compliance with coding standards and technical design."
            )
        ),
        JobOpening(
            title = "IT Infrastructure Specialist",
            company = "Duopharma Biotech Bhd Group of Companies.",
            location = "Petaling Jaya, Selangor",
            type = "Full time",
            salary = "RM 3,000 - RM 4,000 per month",
            logoRes = R.drawable.duopharma_logo,
            description = listOf(
                "Monitor, manage, and troubleshoot the organization's IT infrastructure.",
                "Handle network administration and routine server maintenance.",
                "Ensure high availability and security of our internal systems."
            )
        )
    )
}