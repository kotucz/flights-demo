package cz.kotu.flights.flights

import com.jakewharton.rxrelay2.BehaviorRelay
import cz.kotu.flights.SkypickerService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers.io

class FlightsController(
    private val flightsService: SkypickerService
) {
    private val flightsRelay = BehaviorRelay.create<List<Flight>>()
    private val messageRelay = BehaviorRelay.create<String>()

    val flights: Observable<List<Flight>> get() = flightsRelay.hide()
    val message: Observable<String> get() = messageRelay.hide()

    init {
        flightsService.getFlights(
            dateFrom = "03/04/2018",
            dateTo = "03/05/2018"
        ).subscribeOn(io())
            .observeOn(mainThread())
            .map { it.flights }
            .subscribe(
                flightsRelay,
                Consumer {
                    messageRelay.accept(it.toString())
                })
    }
}