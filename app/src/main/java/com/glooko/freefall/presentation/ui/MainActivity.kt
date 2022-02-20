package com.glooko.freefall.presentation.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.glooko.freefall.service.FreeFallDetectionService

class MainActivity : ComponentActivity() {

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, FreeFallDetectionService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeFallScreen()
        }
    }
}

