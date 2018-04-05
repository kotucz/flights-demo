package cz.kotu.flights

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.kotu.flights.di.FlightsModule
import cz.kotu.flights.flights.FlightsController
import cz.kotu.flights.ui.FlightsPagerAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val flightsPagerAdapter = FlightsPagerAdapter()

    private lateinit var flightsController: FlightsController

    private lateinit var subscriptions: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flightsController = FlightsModule(application).flightsController

        flights.offscreenPageLimit = 5 // preload images
        flights.adapter = flightsPagerAdapter
    }

    override fun onResume() {
        super.onResume()

        subscriptions = CompositeDisposable(
            flightsController.message.subscribe(messageLabel::setText),
            flightsController.flights
                .subscribe({
                    flightsPagerAdapter.items = it
                })
        )
    }

    override fun onPause() {
        subscriptions.dispose()
        super.onPause()
    }
}
