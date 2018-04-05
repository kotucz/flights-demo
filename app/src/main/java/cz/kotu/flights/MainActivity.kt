package cz.kotu.flights

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.kotu.flights.di.MainActivityScopeHolder
import cz.kotu.flights.flights.FlightsController
import cz.kotu.flights.ui.FlightsPagerAdapter
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.LocalDate

class MainActivity : AppCompatActivity() {
    private val flightsPagerAdapter = FlightsPagerAdapter()

    private lateinit var flightsController: FlightsController

    private lateinit var subscriptions: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flightsController =
                ViewModelProviders.of(this).get(MainActivityScopeHolder::class.java).module.flightsController

        flights.offscreenPageLimit = 5 // preload images
        flights.adapter = flightsPagerAdapter
    }

    override fun onResume() {
        super.onResume()

        flightsController.checkUpdate(LocalDate.now())

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
