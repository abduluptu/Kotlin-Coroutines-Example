package com.abdul.khalid.kotlincoroutinesexample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val TAG: String = "KOTLIN_EXAMPLE"

    init {
        viewModelScope.launch {
            while (true){
                delay(500) // 0.5 second
                Log.d(TAG, "Hello From Abdul Bhai")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "View Model Destroyed")
    }
}