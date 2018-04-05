package cz.kotu.flights.data

import android.app.Application
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.github.salomonbrys.kotson.typeToken
import com.squareup.moshi.Moshi
import cz.kotu.flights.flights.Flight
import cz.kotu.flights.flights.FlightsRepository
import org.threeten.bp.LocalDate

class PreferencesFlightsRepository(context: Application) : FlightsRepository {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val rxPreferences = RxSharedPreferences.create(preferences)
    private val moshi: Moshi = Moshi.Builder().build()

    override val offersDate = rxPreferences.getObject<LocalDate>("offersDate", LocalDate.MIN, moshiConverter())
    override val offersFlights = rxPreferences.getObject<List<Flight>>("offersFlights", listOf(), moshiConverter())
    override val previousFlights = rxPreferences.getObject<List<Flight>>("previousFlights", listOf(), moshiConverter())

    private inline fun <reified T : Any> moshiConverter(): Preference.Converter<T> {
        return object : Preference.Converter<T> {
            val innerAdapter = moshi.adapter<T>(typeToken<T>())

            override fun deserialize(serialized: String): T = innerAdapter.fromJson(serialized)

            override fun serialize(value: T): String = innerAdapter.toJson(value)
        }
    }
}