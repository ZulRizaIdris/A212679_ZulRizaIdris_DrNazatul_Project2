package com.example.a212679_zulrizaidris_drnazatul_project2.data

import android.content.Context

interface AppContainer {
    val applicationRepository: ApplicationRepository
    val jobApiRepository: JobApiRepository
    val firestoreRepository: FirestoreRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val applicationRepository: ApplicationRepository by lazy {
        OfflineApplicationRepository(CareerPulseDatabase.getDatabase(context).applicationDao())
    }
    override val jobApiRepository: JobApiRepository by lazy {
        RemoteJobApiRepository()
    }
    override val firestoreRepository: FirestoreRepository by lazy {
        FirestoreRepository()
    }
}