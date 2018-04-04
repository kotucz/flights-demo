package cz.kotu.flights

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cz.kotu.flights.flights.Flight
import cz.kotu.flights.ui.FlightsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val flightsPagerAdapter = FlightsPagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flights.adapter = flightsPagerAdapter

        flightsPagerAdapter.items = listOf(
            Flight("Kravaře", 169.42, 289.0),
            Flight("Amsterdam", 1080.5, 6_293.0)
        )
    }
}
