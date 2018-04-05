package cz.kotu.flights.ui

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.github.florent37.picassopalette.PicassoPalette
import com.squareup.picasso.Picasso
import cz.kotu.flights.R
import cz.kotu.flights.flights.Flight
import cz.kotu.flights.utils.ViewPagerAdapter
import cz.kotu.flights.utils.inflate
import kotlinx.android.synthetic.main.flight.view.*
import okhttp3.HttpUrl

class FlightsPagerAdapter : ViewPagerAdapter() {
    private val defaultTextColor = 0 // black
    private val defaultBackgroundColor = 0xFFFFFFFF // white

    var items: List<Flight> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun createPageView(container: ViewGroup, position: Int): View =
        container.inflate(R.layout.flight).apply {
            items[position].let { flight ->
                val resolvedUrl = "https://images.kiwi.com/photos/600/${flight.photoId}.jpg"
                Picasso.with(context)
                    .load(resolvedUrl)
                    .into(imageView, PicassoPalette.with(resolvedUrl, imageView)
                        .intoCallBack { palette ->
                            val swatch = palette.dominantSwatch
                            backgroundView.setBackgroundColor(swatch?.rgb ?: defaultTextColor)
                            listOf(cityLabel, priceLabel).forEach {
                                it.setTextColor(swatch?.titleTextColor ?: defaultTextColor)
                            }
                            listOf(hashtagsLabel, distanceLabel).forEach {
                                it.setTextColor(swatch?.bodyTextColor ?: defaultTextColor)
                            }
                            palette.vibrantSwatch?.let {
                                goButton.setBackgroundColor(it.rgb)
                                goButton.setTextColor(it.titleTextColor)
                            }
                        })
                cityLabel.text = flight.city
                priceLabel.text = flight.price.toString()
                hashtagsLabel.text = flight.hashtags.joinToString(separator = " ") { "#$it" }
                distanceLabel.text = "${flight.distance}km"
                goButton.setOnClickListener {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                HttpUrl.parse("https://www.kiwi.com/en/booking")!!.newBuilder()
                                    .addQueryParameter("token", flight.bookingToken)
                                    .build().toString()
                            )
                        )
                    )
                }
            }
        }

    override fun getCount(): Int = items.size
}