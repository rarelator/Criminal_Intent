package com.example.criminal_intent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

// private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
//    val crimes = mutableListOf<Crime>()
//    val crimes = crimeRepository.getCrimes()
    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())

    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init{
//        Log.d(TAG, "about to initialize our crime data")
        viewModelScope.launch{
//            Log.d(TAG, "coroutine launched, expect a delay")
//            crimes += loadCrimes()
//            Log.d(TAG, "Crime data should be finished")
        }

    }

//    suspend fun loadCrimes() : List<Crime> {
////        delay(5000) // <- this causes our app to be blank
////        val result = mutableListOf<Crime>()
////        for (i in 0 until 100) {
////            val crime = Crime(
////                id = UUID.randomUUID(),
////                title = "Crime #$i",
////                date = Date(),
////                isSolved = i % 2 == 0
////            )
////            result.add(crime)
////        }
////        return result
//        return crimeRepository.getCrimes()
//    }
}
