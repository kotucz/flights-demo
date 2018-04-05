package cz.kotu.flights.flights

import com.squareup.moshi.Json

data class FlightsResponse(
    @Json(name = "currency") val currency: String,
    @Json(name = "data") val flights: List<Flight>
)

data class Flight(
    @Json(name = "id") val id: String,
    @Json(name = "cityTo") val city: String,
    @Json(name = "booking_token") val bookingToken: String,
    @Json(name = "distance") val distance: Double,
    @Json(name = "mapIdto") val photoId: String,
    @Json(name = "price") val price: Double
)