package com.example.criminal_intent

import android.app.Application

// allows us to have access to the creation of the application
class CriminalIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}