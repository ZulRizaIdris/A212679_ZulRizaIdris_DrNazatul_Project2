package com.example.a212679_zulrizaidris_drnazatul_project2.data

interface JobApiRepository {
    suspend fun searchJobs(query: String): List<RemoteJob>
}

class RemoteJobApiRepository : JobApiRepository {
    override suspend fun searchJobs(query: String): List<RemoteJob> {
        return try {
            JobRetrofitClient.instance.getJobs(search = query).jobs
        } catch (e: Exception) {
            emptyList()
        }
    }
}