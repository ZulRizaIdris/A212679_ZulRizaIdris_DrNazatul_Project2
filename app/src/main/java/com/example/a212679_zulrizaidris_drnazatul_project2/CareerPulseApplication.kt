package com.example.a212679_zulrizaidris_drnazatul_project2

import android.app.Application
import com.example.a212679_zulrizaidris_drnazatul_project2.data.AppContainer
import com.example.a212679_zulrizaidris_drnazatul_project2.data.AppDataContainer

class CareerPulseApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}