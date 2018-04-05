package cz.kotu.flights.flights

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.kotu.flights.SkypickerService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers.io
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class FlightsController(
    private val dateFormatter: DateTimeFormatter,
    private val flightsService: SkypickerService,
    private val repository: FlightsRepository
) {
    private val messageRelay = BehaviorRelay.create<String>()

    val flights: Observable<List<Flight>> = repository.offersFlights.asObservable()
    val message: Observable<String> get() = messageRelay.hide()

    init {
        val today = LocalDate.now()

        if (repository.offersDate.get().isBefore(today)) {
            refreshOffers(today)
        }
    }

    private fun refreshOffers(today: LocalDate) {
        val end = today.plusMonths(1)
        flightsService.getFlights(
            dateFrom = dateFormatter.format(today),
            dateTo = dateFormatter.format(end)
        ).subscribeOn(io())
            .observeOn(mainThread())
            .map { it.flights }
            .subscribe(
                repository.offersFlights.asConsumer(),
                Consumer {
                    messageRelay.accept(it.toString())
                })
    }
}