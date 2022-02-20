package com.glooko.freefall.data

import android.hardware.SensorEvent
import kotlinx.coroutines.flow.Flow

/**
 * Free fall sensor data source to handle sensor operation
 */
interface FreeFallSensorDataSource {
    /**
     * method to getting the updates for sensor changes
     */
    fun observeOnFreeFallEvent(): Flow<SensorEvent>
}