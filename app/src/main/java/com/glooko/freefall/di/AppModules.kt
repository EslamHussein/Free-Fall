package com.glooko.freefall.di

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.room.Room
import com.glooko.freefall.data.FreeFallRepositoryImpl
import com.glooko.freefall.data.FreeFallSensorDataSource
import com.glooko.freefall.data.LocalDataSource
import com.glooko.freefall.domain.FreeFallRepository
import com.glooko.freefall.domain.usecase.AddFreeFallEventUseCase
import com.glooko.freefall.domain.usecase.GetAllFreeFallEventsUseCase
import com.glooko.freefall.domain.usecase.ObserveOnFreeFallEventUseCase
import com.glooko.freefall.data.localdatasource.FreeFallDao
import com.glooko.freefall.data.localdatasource.FreeFallDatabase
import com.glooko.freefall.data.localdatasource.LocalDataSourceImpl
import com.glooko.freefall.presentation.FreeFallViewModel
import com.glooko.freefall.data.sensordatasource.FreeFallSensorDataSourceImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


val dataModule = module {
    single<FreeFallRepository> { FreeFallRepositoryImpl(get(), get()) }
}

val localModule = module {

    single<LocalDataSource> { LocalDataSourceImpl(get()) }

    single {
        Room.databaseBuilder(
            androidApplication(),
            FreeFallDatabase::class.java, "FREE-FALL-DB"
        ).build()
    }
    single {
        get<FreeFallDatabase>().freeFallDao() as FreeFallDao
    }
}


val domainModule = module {
    single { GetAllFreeFallEventsUseCase(get()) }
    single { AddFreeFallEventUseCase(get()) }
    single { ObserveOnFreeFallEventUseCase(get()) }
}
val sensorModule = module {

    single {
        androidContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    single<Sensor> { get<SensorManager>().getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    single<FreeFallSensorDataSource> { FreeFallSensorDataSourceImpl(get(), get()) }
}

val presentationModule = module {
    viewModel {
        FreeFallViewModel(get(), get(), get())
    }
    factory { Dispatchers.IO }
}
