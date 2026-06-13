package com.example.a212679_zulrizaidris_drnazatul_project2.data

import com.google.gson.annotations.SerializedName

data class RemoteJobResponse(
    val jobs: List<RemoteJob>
)

data class RemoteJob(
    val id: Int = 0,
    val url: String = "",
    val title: String = "",

    @SerializedName("company_name")
    val companyName: String = "",

    @SerializedName("candidate_required_location")
    val location: String = "",

    val description: String = "",
    val salary: String = "",

    @SerializedName("job_type")
    val jobType: String = ""
)