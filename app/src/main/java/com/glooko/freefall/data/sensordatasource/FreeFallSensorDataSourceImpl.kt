package com.glooko.freefall.data.sensordatasource

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.elvishew.xlog.XLog
import com.glooko.freefall.data.FreeFallSensorDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FreeFallSensorDataSourceImpl(
    private val sensorManager: SensorManager,
    private val accelerometerSensor: Sensor
) : FreeFallSensorDataSource {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeOnFreeFallEvent(): Flow<SensorEvent> {
        return callbackFlow {
            val listener: SensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    XLog.d("FreeFallSensorDataSourceImpl::observeOnFreeFallEvent, SensorEvent: $event")
                    trySendBlocking(event)
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

                }
            }
            sensorManager.registerListener(
                listener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            awaitClose {
                sensorManager.unregisterListener(listener, accelerometerSensor)
            }
        }
    }
}