package cz.kotu.flights.flights

import android.content.res.Resources
import com.jakewharton.rxrelay2.BehaviorRelay
import cz.kotu.flights.R
import cz.kotu.flights.SkypickerService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers.io
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

const val dailyOffersCount = 5

class FlightsController(
    private val resources: Resources,
    private val dateFormatter: DateTimeFormatter,
    private val flightsService: SkypickerService,
    private val repository: FlightsRepository
) {
    private val messageRelay = BehaviorRelay.createDefault<String>("")

    val flights: Observable<List<Flight>> = repository.offersFlights.asObservable()
    val message: Observable<String> get() = messageRelay.hide()

    internal fun checkUpdate(today: LocalDate) {
        if (repository.offersDate.get().isBefore(today)) {
            refreshOffers(today)
        }
    }

    private fun refreshOffers(today: LocalDate) {
        repository.previousFlights.set(repository.previousFlights.get() + repository.offersFlights.get())
        repository.offersFlights.delete()
        messageRelay.accept(resources.getString(R.string.loading))

        val end = today.plusMonths(1)
        flightsService.getFlights(
            dateFrom = dateFormatter.format(today),
            dateTo = dateFormatter.format(end)
        ).subscribeOn(io())
            .observeOn(mainThread())
            .map {
                it.flights
                    .filter { wasNotOffered(it) }
                    .take(dailyOffersCount)
            }
            .doAfterSuccess {
                repository.offersDate.set(today)
                messageRelay.accept("")
            }
            .subscribe(
                repository.offersFlights.asConsumer(),
                Consumer {
                    messageRelay.accept(it.toString())
                })
    }

    private fun wasNotOffered(flight: Flight): Boolean =
        repository.previousFlights.get().find { it.id == flight.id } == null
}