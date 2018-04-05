package cz.kotu.flights.flights

import com.f2prateek.rx.preferences2.Preference
import org.threeten.bp.LocalDate

interface FlightsRepository {
    val offersDate: Preference<LocalDate>
    val offersFlights: Preference<List<Flight>>
    val previousFlights: Preference<List<Flight>>
}