package com.glooko.freefall.data

import android.hardware.Sensor
import com.glooko.freefall.domain.FreeFallRepository
import com.glooko.freefall.domain.usecase.AddFreeFallEventUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.sqrt

class FreeFallRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val sensorDataSource: FreeFallSensorDataSource
) : FreeFallRepository {

    override fun addFreeFallEvent(params: AddFreeFallEventUseCase.Params) =
        localDataSource.addFreeFallEvent(params.timeStamp)

    override fun getAllFreeFAllEvents() = localDataSource.getAllFreeFAllEvents()

    override fun observeOnFreeFallChanges(): Flow<String> {
        return sensorDataSource.observeOnFreeFallEvent()
            .filter { it.sensor.type == Sensor.TYPE_ACCELEROMETER }
            .map { event ->
                val loX = event.values[0].toDouble()
                val loY = event.values[1].toDouble()
                val loZ = event.values[2].toDouble()
                val loAccelerationReader = sqrt(
                    loX.pow(2.0)
                            + loY.pow(2.0)
                            + loZ.pow(2.0)
                )
                val precision = DecimalFormat("0.00")
                precision.format(loAccelerationReader).toDouble()
            }
            .filter {
                it > 0.3 && it < 0.5
            }
            .map {
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) / 3
            }
            .distinctUntilChanged()
            .map {
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).format(Date())
            }
    }
}