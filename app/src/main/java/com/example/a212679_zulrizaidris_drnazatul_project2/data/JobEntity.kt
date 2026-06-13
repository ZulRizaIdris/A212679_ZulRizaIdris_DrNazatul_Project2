package com.example.a212679_zulrizaidris_drnazatul_project2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val jobTitle: String,
    val applicantName: String,
    val interviewDate: String,
    val company: String = "",
    val status: String = "Pending"

)
