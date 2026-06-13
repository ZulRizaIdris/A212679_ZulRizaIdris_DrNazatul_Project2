package com.example.a212679_zulrizaidris_drnazatul_project2.data

import kotlinx.coroutines.flow.Flow

interface ApplicationRepository {
    fun getAllApplicationsStream(): Flow<List<JobEntity>>
    suspend fun insertApplication(application: JobEntity)
}

class OfflineApplicationRepository(private val applicationDao: ApplicationDao) : ApplicationRepository {
    override fun getAllApplicationsStream(): Flow<List<JobEntity>> = applicationDao.getAllApplications()
    override suspend fun insertApplication(application: JobEntity) = applicationDao.insert(application)
}