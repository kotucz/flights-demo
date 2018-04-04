package cz.kotu.flights

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.kotu.flights.di.FlightsModule
import cz.kotu.flights.ui.FlightsPagerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val flightsPagerAdapter = FlightsPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flights.adapter = flightsPagerAdapter

        FlightsModule().flightsService.getFlights(
            dateFrom = "03/04/2018",
            dateTo = "03/05/2018"
        ).subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                flightsPagerAdapter.items = it.flights
            }, {
                // TODO show error
            })
    }
}
