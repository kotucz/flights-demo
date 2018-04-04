package cz.kotu.flights.ui

import android.view.View
import android.view.ViewGroup
import cz.kotu.flights.R
import cz.kotu.flights.flights.Flight
import cz.kotu.flights.utils.ViewPagerAdapter
import cz.kotu.flights.utils.inflate
import kotlinx.android.synthetic.main.flight.view.*

class FlightsPagerAdapter : ViewPagerAdapter() {
    var items: List<Flight> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createPageView(container: ViewGroup, position: Int): View =
        container.inflate(R.layout.flight).apply {
            items[position].let { flight ->
                // TODO load imageView
                cityLabel.text = flight.city
                priceLabel.text = flight.price.toString()
            }
        }

    override fun getCount(): Int = items.size
}