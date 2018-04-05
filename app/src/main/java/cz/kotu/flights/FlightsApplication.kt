package cz.kotu.flights

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class FlightsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}