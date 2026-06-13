package com.example.a212679_zulrizaidris_drnazatul_project2.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface JobApiService {
    // Free Remotive API — no API key required
    @GET("api/remote-jobs")
    suspend fun getJobs(
        @Query("category") category: String = "software-dev",
        @Query("search") search: String = "",
        @Query("limit") limit: Int = 20
    ): RemoteJobResponse
}

object JobRetrofitClient {
    private const val BASE_URL = "https://remotive.com/"

    val instance: JobApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JobApiService::class.java)
    }
}