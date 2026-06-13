package com.example.a212679_zulrizaidris_drnazatul_project2.data

data class CommunityPost(
    val id: String = "",
    val authorName: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val tipContent: String = "",
    val timestamp: Long = System.currentTimeMillis()
)