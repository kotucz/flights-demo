package cz.kotu.flights.di

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

internal class MainActivityScopeHolder(application: Application) : AndroidViewModel(application) {
    val module = FlightsModule(application)
}