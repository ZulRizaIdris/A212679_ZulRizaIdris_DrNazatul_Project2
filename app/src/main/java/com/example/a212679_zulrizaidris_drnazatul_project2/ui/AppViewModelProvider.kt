package com.example.a212679_zulrizaidris_drnazatul_project2.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a212679_zulrizaidris_drnazatul_project2.CareerPulseApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = careerPulseApplication()
            CareerPulseViewModel(
                repository = app.container.applicationRepository,
                jobApiRepository = app.container.jobApiRepository,       // ✅ NEW
                firestoreRepository = app.container.firestoreRepository  // ✅ NEW
            )
        }
    }
}

fun CreationExtras.careerPulseApplication(): CareerPulseApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as CareerPulseApplication)