package com.glooko.freefall.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.elvishew.xlog.XLog
import com.glooko.freefall.R
import com.glooko.freefall.domain.usecase.AddFreeFallEventUseCase
import com.glooko.freefall.domain.usecase.ObserveOnFreeFallEventUseCase
import com.glooko.freefall.presentation.ui.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import org.koin.android.ext.android.inject

private const val CHANNEL_ID = "FreeFallDetectionServiceChannel"

/**
 * Foreground service responsible to keep observing on free fall event
 */
class FreeFallDetectionService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private val observeOnFreeFallEventUseCase: ObserveOnFreeFallEventUseCase by inject()
    private val addFreeFallEventUseCase: AddFreeFallEventUseCase by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        XLog.d("FreeFallDetectionService::onCreate, Service created")
        scope.launch {
            observeOnFreeFallEventUseCase.execute()
                .flatMapLatest {
                    addFreeFallEventUseCase.execute(
                        AddFreeFallEventUseCase.Params.create(
                            it
                        )
                    )
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    XLog.d("FreeFallDetectionService::onCreate, free fall event collected")
                }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        XLog.d("FreeFallDetectionService::onStartCommand, service stared with intent: $intent , flags: $flags , startId: $startId")
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        createNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.free_fall_detection))
            .setContentText(getString(R.string.free_fall_detection_service))
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.free_fall_detection_service),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        XLog.d("FreeFallDetectionService::onDestroy, Service destroyed")
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}